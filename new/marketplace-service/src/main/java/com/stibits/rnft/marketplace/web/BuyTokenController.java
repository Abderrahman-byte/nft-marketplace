package com.stibits.rnft.marketplace.web;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.validation.Valid;

import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Connection;
import com.stibits.rnft.common.api.ApiResponse;
import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.api.ProfileDetails;
import com.stibits.rnft.common.api.RequestWithId;
import com.stibits.rnft.common.errors.ApiError;
import com.stibits.rnft.common.errors.DataIntegrityError;
import com.stibits.rnft.common.helpers.IpfsService;
import com.stibits.rnft.common.utils.AESUtils;
import com.stibits.rnft.marketplace.api.QrcodeScannedRequest;
import com.stibits.rnft.marketplace.api.StreamReferencePayload;
import com.stibits.rnft.marketplace.domain.Token;
import com.stibits.rnft.marketplace.errors.TokenNotFoundError;
import com.stibits.rnft.marketplace.repositories.TokenRepository;
import com.stibits.rnft.marketplace.services.TransactionService;
import com.stibits.rnft.marketplace.streams.TransactionStreamExecutor;
import com.stibits.rnft.marketplace.utils.BasicRmqPublisher;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/buy")
@Slf4j
public class BuyTokenController {
    private final long STREAM_TIMEOUT = 15 * 60 * 1000; // 5 minutes timeout

    @Value("${jwt.shared.secret}")
    private String jwtSecret;

    @Value("${aes.password}")
    private String aesPassword;

    @Autowired
    private IpfsService ipfsService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Connection rmqConnection;

    @Autowired
    private BasicRmqPublisher rmqPublisher;

    private SecretKey secretKey;

    private IvParameterSpec ivParameterSpec;

    @PostConstruct
    public void init () {
        byte iv[] = Arrays.copyOf(this.aesPassword.getBytes(), 16);
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.ivParameterSpec = AESUtils.generateIv(iv);
    }

    @PostMapping
    public ApiResponse createBuyTokenStream (
        @AuthenticationPrincipal ProfileDetails account, 
        @RequestBody @Valid RequestWithId body) throws ApiError {

        if (account == null) return ApiResponse.getFailureResponse();

        Token token = tokenRepository.selectById(body.getId());

        if (token == null) throw new TokenNotFoundError();

        String ownerId = transactionService.getTokenOwner(token);

        if (ownerId.equals(account.getId())) throw new DataIntegrityError("You cannot buy your own token", "id");

        if (!token.getSettings().isInstantSale()) throw new DataIntegrityError("Token is not for sell", "id");

        // TODO : should check the acccount RVN balance

        StreamReferencePayload ref = new StreamReferencePayload();
        ref.expiresAfterMinutes(2);
        ref.setAction("transaction");
        ref.setPreviewUrl(ipfsService.resolveHashUrl(token.getPreviewUrl()));
        ref.setFromId(ownerId);
        ref.setToId(account.getId());
        ref.setTokenId(body.getId());
        ref.setTokenTitle(token.getTitle());
        ref.setPrice(token.getSettings().getPrice());

        String refJwt = this.generateJwtReference(ref);

        return new ApiSuccessResponse<>(Map.of("ref", refJwt));
    }

    @GetMapping
    public SseEmitter buyTokenStream (@RequestParam("ref") String ref) {
        SseEmitter emitter = new SseEmitter(STREAM_TIMEOUT);
        ExecutorService sseExecutor = Executors.newSingleThreadExecutor();
        TransactionStreamExecutor executor = new TransactionStreamExecutor(emitter, rmqConnection);

        executor.setObjectMapper(objectMapper);
        executor.setJwtSecretKey(secretKey);
        executor.setRefrence(ref);
        executor.setEncryptionKey(aesPassword);
        executor.setTransactionService(transactionService);

        sseExecutor.execute(executor);

        emitter.onCompletion(() -> {
            executor.complete();
        });

        emitter.onTimeout(() -> {
            try {
                executor.close();
            } catch (Exception ex) {
                executor.complete();
            }
        });

        return emitter;
    }

    @PostMapping("/scanned")
    @SuppressWarnings("unchecked")
    public ApiResponse buyQrCodeScanned (@RequestBody @Valid QrcodeScannedRequest request) {
        if (!StringUtils.hasText(request.getCode())) return ApiResponse.getFailureResponse();

        String decryptedString = AESUtils.decryptToString(this.aesPassword, request.getCode(), this.ivParameterSpec);

        log.info("decryptedString : " + decryptedString);

        if (!StringUtils.hasText(decryptedString)) return ApiResponse.getFailureResponse();

        try {
            Map<String, Object> payload = this.objectMapper.readValue(decryptedString, Map.class);
            String routingKey = (String)payload.get("routingKey");

            if (!StringUtils.hasText(routingKey)) return ApiResponse.getFailureResponse();

            boolean published = this.rmqPublisher.publish("amq.direct", routingKey, objectMapper.writeValueAsBytes(Map.of("accepted", request.isAccepted())));

            return new ApiResponse() {
                public boolean isSuccess() {
                    return published;
                }
            };
        } catch (JsonProcessingException ex) {}

        return ApiResponse.getFailureResponse();
    }

    private String generateJwtReference (StreamReferencePayload ref) {
        try {
            String payload = objectMapper.writeValueAsString(ref);

            return Jwts.builder()
               .setPayload(payload)
                .signWith(secretKey)
                .compact();
        } catch (JsonProcessingException ex) {
            log.info("generateJwtReference : " + ex.getMessage());
            return null;
        }
    }
}
