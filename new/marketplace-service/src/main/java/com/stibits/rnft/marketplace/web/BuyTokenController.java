package com.stibits.rnft.marketplace.web;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stibits.rnft.common.api.ApiResponse;
import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.api.ProfileDetails;
import com.stibits.rnft.common.api.RequestWithId;
import com.stibits.rnft.common.errors.ApiError;
import com.stibits.rnft.common.errors.DataIntegrityError;
import com.stibits.rnft.marketplace.api.StreamReferencePayload;
import com.stibits.rnft.marketplace.domain.Token;
import com.stibits.rnft.marketplace.errors.TokenNotFoundError;
import com.stibits.rnft.marketplace.repositories.TokenRepository;
import com.stibits.rnft.marketplace.services.TransactionService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/buy")
@Slf4j
public class BuyTokenController {
    @Value("${jwt.shared.secret}")
    private String jwtSecret;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private SecretKey secretKey;

    @PostConstruct
    public void getSecretKey () {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @PostMapping
    public ApiResponse buyToken (
        @AuthenticationPrincipal ProfileDetails account, 
        @RequestBody @Valid RequestWithId body) throws ApiError {

        if (account == null) return ApiResponse.getFailureResponse();

        Token token = tokenRepository.selectById(body.getId());

        if (token == null) throw new TokenNotFoundError();

        String onwerId = transactionService.getTokenOwner(token);

        if (onwerId.equals(account.getId())) throw new DataIntegrityError("You cannot buy your own token", "id");

        if (!token.getSettings().isInstantSale()) throw new DataIntegrityError("Token is not for sell", "id");

        StreamReferencePayload ref = new StreamReferencePayload(account.getId(), onwerId, "transaction", token.getSettings().getPrice());
        String refJwt = this.generateJwtReference(ref);

        return new ApiSuccessResponse<>(Map.of("ref", refJwt));
    }

    private String generateJwtReference (StreamReferencePayload ref) {
        try {
            String payload = objectMapper.writeValueAsString(ref);

            log.info("Payload generated : " + payload);
            
            
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
