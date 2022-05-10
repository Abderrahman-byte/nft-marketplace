package org.stibits.rnft.executors;

import java.io.IOException;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.AuthenticationRequiredError;
import org.stibits.rnft.errors.InvalidData;
import org.stibits.rnft.errors.UnauthorizedError;
import org.stibits.rnft.repositories.NFTokenDAO;
import org.stibits.rnft.repositories.TransactionDAO;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionSseExecutor implements Runnable {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	private SseEmitter emitter;
	private String jwtSecret;
	private String refToken;
	private Account account;
	private TransactionDAO transDAO;
	
	
	private NFTokenDAO tokenDao;
	

	public static void setObjectMapper(ObjectMapper objectMapper) {
		TransactionSseExecutor.objectMapper = objectMapper;
	}
	public void setEmitter(SseEmitter emitter) {
		this.emitter = emitter;
	}
	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}
	public void setRefToken(String refToken) {
		this.refToken = refToken;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	public void setTransDAO(TransactionDAO transDAO) {
		this.transDAO = transDAO;
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
	
	private void execute()throws Exception{
		
		 Map<String, Claim> payload = this.parseRefJwt(this.refToken);

	        if (account == null) {
	            this.sendAndComplete(this.makeErrorEvent(new AuthenticationRequiredError()));
	            return ;
	        }

	        if (payload == null || !this.checkPayload(payload)) {
	            this.sendAndComplete(this.makeErrorEvent(new InvalidData()));
	            return ;
	        }
	        
	        String accountId = payload.get("accountToId").asString();
	        String tokenId = payload.get("tokenId").asString();
	        String fromId = payload.get("accountFrom").asString();
	        double price = payload.get("price").asDouble();
	        
	        if (!accountId.equals(account.getId())) {
	            this.sendAndComplete(this.makeErrorEvent(new UnauthorizedError()));
	            return ;
	        }

	        
	        String qrCode = accountId + ":" + fromId + ":" + tokenId + ":" + price;
	        qrCode = Base64.encodeBase64String(qrCode.getBytes());

	        Thread.sleep(1000);

	        this.emitter.send(SseEmitter.event().name("qr").data(qrCode));
	        
	        Thread.sleep(5000);

	        this.transDAO.insertTransaction(tokenId, fromId, accountId, price);

	        this.emitter.send(SseEmitter.event().name("accepted").data(""));
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
	 private SseEventBuilder makeErrorEvent (ApiError error) throws JsonProcessingException {
	        return SseEmitter.event()
	            .name("error")
	            .data(objectMapper.writeValueAsString(error));
	    }

	    private void sendAndComplete (SseEventBuilder event) throws IOException {
	        this.emitter.send(event);
	        this.emitter.complete();
	    }

	    private boolean checkPayload (Map<String, Claim> payload) {
	        return payload != null && payload.containsKey("tokenId") && payload.get("tokenId") != null &&
	            payload.containsKey("accountFrom") && payload.get("accountFrom") != null && 
	            payload.containsKey("accountToId") && payload.get("accountToId") != null&&
	            payload.containsKey("price") && payload.get("price") != null;
	           
	    } 


}
