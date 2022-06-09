package com.stibits.rnft.marketplace.streams;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.util.Arrays;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;
import com.stibits.rnft.common.errors.ApiError;
import com.stibits.rnft.common.errors.InvalidPayloadError;
import com.stibits.rnft.common.utils.AESUtils;
import com.stibits.rnft.common.utils.RandomStringGenerator;
import com.stibits.rnft.commonweb.streams.AbstractStreamRunnable;
import com.stibits.rnft.marketplace.api.StreamReferencePayload;
import com.stibits.rnft.marketplace.services.TransactionService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

// TODO : delete queue before connection closed

@Slf4j
public class TransactionStreamExecutor extends AbstractStreamRunnable {
    @Setter private SecretKey jwtSecretKey;
    @Setter private String refrence;
    @Setter private TransactionService transactionService;
    private String encryptionKey;
    private IvParameterSpec ivParameterSpec;
    private Connection rmqConnection;
    private Channel rmqChannel;
    private String queue;

    public TransactionStreamExecutor (SseEmitter emitter, Connection rmqConnection) {
        this.sseEmitter = emitter;
        this.rmqConnection = rmqConnection;

        try {
            this.rmqChannel = this.rmqConnection.createChannel();
            this.rmqChannel.basicQos(1);
            this.queue = this.rmqChannel.queueDeclare("", false, true, false, Map.of()).getQueue();
        } catch (IOException ex) {
            this.setCompleted(true);
            this.sseEmitter.completeWithError(ex);
            log.error(ex.getMessage(), ex);
        }
    }

    public void setEncryptionKey(String encryptionKey) {
        byte iv[] = Arrays.copyOf(encryptionKey.getBytes(), 16);
        this.encryptionKey = encryptionKey;
        this.ivParameterSpec = AESUtils.generateIv(iv);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void execute() throws Exception {
        Map<String, Object> payload = this.getPayload(this.refrence);
        StreamReferencePayload referencePayload = this.objectMapper.convertValue(payload, StreamReferencePayload.class);
        String routingKey = RandomStringGenerator.generateStr(30);
        boolean accepted = false;
        payload.put("routingKey", routingKey);

        if (referencePayload == null || !"transaction".equals(referencePayload.getAction())) {
            this.sendErrorAndComplete(new InvalidPayloadError());
            this.closeRmqChannel();
            return;
        }

        // Create RabbitMq queue
        this.rmqChannel.queueBind(this.queue, "amq.direct", routingKey);

        // Generate Qr code
        String qrCode = this.generateQrCode(payload);
        this.sendDataEvent("qr", qrCode);
        
        // Wait for user to scan
        while (this.rmqChannel.isOpen() && !this.getCompleted()) {
            try {
                GetResponse response = this.rmqChannel.basicGet(queue, false);
                
                if (response == null) {
                    Thread.sleep(500);
                    continue;
                }

                this.rmqChannel.basicAck(response.getEnvelope().getDeliveryTag(), true);
                
                Map<String, Object> responseBody = (Map<String, Object>)this.objectMapper.readValue(response.getBody(), Map.class);

                // FIXME : The message should contain more information about how scanned the qrCode
                if (responseBody != null && responseBody.get("accepted") != null && responseBody.get("accepted").getClass().equals(Boolean.class)) {
                    accepted = (Boolean)responseBody.get("accepted");
                }

                break;
            } catch (IOException ex) {
                this.setCompleted(true);
                break;
            }
        }
        

        this.closeRmqChannel();

        if (!accepted || this.getCompleted()) {
            this.close();
            return ;
        }

        try {
            this.transactionService.createTransaction(
                referencePayload.getTokenId(), 
                referencePayload.getFromId(), 
                referencePayload.getToId(), 
                referencePayload.getPrice());
        
            this.sendDataEvent("accepted", "");

            // This should should trigger a transactions created event (for : notifications)
        } catch (ApiError error) {
            this.sendErrorAndComplete(error);
        }
    }

    @Override
    public synchronized void complete() {
        this.setCompleted(true);
        this.closeRmqChannel();
    }

    @Override
    public synchronized void close() throws Exception {
        this.setCompleted(true);
        this.sendDataEvent("close", "");
        this.sseEmitter.complete();
    }

    public synchronized void closeRmqChannel () {
        if (this.rmqChannel.isOpen()) {
            try {
                this.rmqChannel.queueDelete(this.queue);
                this.rmqChannel.close();
            } catch (IOException | TimeoutException ex) {}
        }
    }

    private Map<String, Object> getPayload (String ref) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.jwtSecretKey)
                .build()
                .parseClaimsJws(ref)
                .getBody();

            return claims;
        } catch (JwtException ex) {
            log.info("Invalid JWT : " + ref);
        } catch (IllegalArgumentException ex) {
            log.info("Invalid Payload : " + ref);
        }

        return null;
    }

    private String generateQrCode (Map<String, Object> reference) {
        Calendar expires = Calendar.getInstance();

        expires.add(Calendar.MINUTE, 1);
        reference.put("iat", Calendar.getInstance().getTimeInMillis() / 1000);
        reference.put("exp", expires.getTimeInMillis() / 1000);

        try {
            String payload = this.objectMapper.writeValueAsString(reference);
            String encrypted = AESUtils.encrypt(this.encryptionKey, payload, this.ivParameterSpec);

            return encrypted;
        } catch (JsonProcessingException ex) {
            log.error("[ERROR-generateQrCode] " + ex.getMessage(), ex);
            return null;
        }
    }
}
