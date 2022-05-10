package org.stibits.rnft.executors;

import java.io.IOException;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.AuthenticationRequiredError;
import org.stibits.rnft.errors.InvalidData;
import org.stibits.rnft.errors.UnauthorizedError;
import org.stibits.rnft.repositories.BidsDAO;

public class CreateBidSseExecutor implements Runnable {
    private static ObjectMapper objectMapper = new ObjectMapper();
    // private final String defaultError = "{\"title\":\"unknown_error\",\"status\":400}";

    private SseEmitter emitter;
    private String jwtSecret;
    private String refToken;
    private Account account;
    private BidsDAO bidsDAO;

    public CreateBidSseExecutor () {}

    public void setRefToken(String refToken) {
        this.refToken = refToken;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setBidsDAO(BidsDAO bidsDAO) {
        this.bidsDAO = bidsDAO;
    }

    public void setEmitter(SseEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void run() {
        try {
            this.execute();
        } catch (Exception ex) {
            System.err.println("[ERROR] " + ex.getMessage());
            this.emitter.complete();
        }
    }
    
    private void execute () throws Exception {
        Map<String, Claim> payload = this.parseRefJwt(this.refToken);

        if (account == null) {
            this.sendAndComplete(this.makeErrorEvent(new AuthenticationRequiredError()));
            return ;
        }

        if (payload == null || !this.checkPayload(payload)) {
            this.sendAndComplete(this.makeErrorEvent(new InvalidData()));
            return ;
        }
        
        String accountId = payload.get("accountId").asString();
        String tokenId = payload.get("tokenId").asString();
        String toId = payload.get("to").asString();
        double price = payload.get("price").asDouble();
        
        if (!accountId.equals(account.getId())) {
            this.sendAndComplete(this.makeErrorEvent(new UnauthorizedError()));
            return ;
        }

        // Receive Qr code from the api
        String qrCode = accountId + ":" + toId + ":" + tokenId + ":" + price;
        qrCode = Base64.encodeBase64String(qrCode.getBytes());

        Thread.sleep(1000);

        this.emitter.send(SseEmitter.event().name("qr").data(qrCode));

        // Wait untill the user accept the bid through qr code scanning
        Thread.sleep(2000);

        try {
            this.bidsDAO.insertBid(account, toId, tokenId, price);
        } catch (ApiError ex) {
            this.sendAndComplete(this.makeErrorEvent(ex));
            return ;
        }

        this.emitter.send(SseEmitter.event().name("accepted").data(""));
        this.emitter.complete();
    }

    private SseEventBuilder makeErrorEvent (ApiError error) throws JsonProcessingException {
        return SseEmitter.event()
            .name("error")
            .data(objectMapper.writeValueAsString(error));
    }

    private void sendAndComplete (SseEventBuilder event) throws IOException {
        this.emitter.send(event);
        this.emitter.complete();
    }

    private Map<String, Claim> parseRefJwt (String ref) {
        Algorithm algorithm = Algorithm.HMAC256(this.jwtSecret);

        try {
            return JWT.require(algorithm).build().verify(ref).getClaims();
        } catch (JWTVerificationException ex) {
            return null;
        }
    }

    private boolean checkPayload (Map<String, Claim> payload) {
        return payload != null && payload.containsKey("tokenId") && payload.get("tokenId") != null &&
            payload.containsKey("to") && payload.get("to") != null && 
            payload.containsKey("accountId") && payload.get("accountId") != null && 
            payload.containsKey("price") && payload.get("price") != null;
    } 
}
