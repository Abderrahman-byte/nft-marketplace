package com.stibits.rnft.gateway.security.jwt;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import com.stibits.rnft.common.api.ProfileDetails;
import com.stibits.rnft.gateway.domain.Account;
import com.stibits.rnft.gateway.domain.AccountDetails;
import com.stibits.rnft.gateway.services.ProfileService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

public class JwtGatewayWebFilter implements GatewayFilter {
    @Value("${jwt.shared.secret}")
    private String jwtSecret;

    private SecretKey secretKey;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ProfileService profileService;

    @PostConstruct
    public void generateSecretKey () {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String bearer = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(bearer) || !bearer.startsWith(JwtSecurityWebFilter.BEARER_PREFIX))
            return chain.filter(exchange);

            
        String token = bearer.substring(JwtSecurityWebFilter.BEARER_PREFIX.length());
        
        if (!StringUtils.hasText(token) || !this.jwtProvider.validateToken(token))
            return chain.filter(exchange);

        Authentication authentication = this.jwtProvider.getAuthentication(token);

        if (authentication == null) return chain.filter(exchange);

        HashMap<String, Object> claims = new HashMap<>();
        String sub;

        if (authentication.getPrincipal() instanceof AccountDetails) {
            AccountDetails accountDetails = (AccountDetails)authentication.getPrincipal();
            Account account = accountDetails.getAccount();
            
            ProfileDetails profileDetails = this.profileService.getProfileDetails(account);

            claims.put("details", profileDetails);
            sub = account.getId();
        } else {
            claims.put("details", authentication.getPrincipal());
            sub = authentication.getName();
        }

        String middlewareToken = this.createToken(sub, claims);

        return chain.filter(exchange.mutate()
                .request(request.mutate()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + middlewareToken)
                    .build())
                .build());
    }

    private String createToken (String sub, Map<String, Object> claims) {
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.MINUTE, 1);

        return Jwts.builder()
            .addClaims(claims)
            .setSubject(sub)
            .setExpiration(expires.getTime())
            .setIssuedAt(new Date())
            .signWith(secretKey)
            .compact();
    }
}
