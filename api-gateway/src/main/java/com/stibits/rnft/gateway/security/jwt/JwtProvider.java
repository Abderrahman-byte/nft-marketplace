package com.stibits.rnft.gateway.security.jwt;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import com.stibits.rnft.gateway.domain.Account;
import com.stibits.rnft.gateway.domain.AccountDetails;
import com.stibits.rnft.gateway.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtProvider {
    @Autowired
    private SecretKey jwtSecretKey;

    @Autowired
    private AccountRepository accountRepository;

    private JwtParser jwtParser;

    @PostConstruct
    public void initJwtParser () {
        this.jwtParser = Jwts.parserBuilder().setSigningKey(jwtSecretKey).build();
    }

    public String createToken (Authentication authentication) {
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.MONTH, 1);

        return this.createToken(authentication, null, expires);
    }

    public String createToken (Authentication authentication, Map<String, Object> claims, Calendar expires) {
        String sub = authentication.getName();

        if (authentication.getPrincipal() instanceof AccountDetails) {
            AccountDetails details = (AccountDetails)authentication.getPrincipal();

            sub = details.getAccount().getId();
        }

        return this.createToken(sub, claims, expires);
    }

    public String createToken (String sub, Map<String, Object> claims, Calendar expires) {
        Date now = new Date();

        if (claims == null) claims = new HashMap<>();

        return Jwts.builder()
            .addClaims(claims)
            .setSubject(sub)
            .setIssuedAt(now)
            .setExpiration(expires.getTime())
            .signWith(jwtSecretKey)
            .compact();
    }

    public boolean validateToken (String token) {
        try {
            jwtParser.parse(token);
            return true;
        } catch (JwtException ex) {
            log.info("Invalid JWT : " + ex.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication (String token) {
        Claims claims = this.parseToken(token);
        
        if (claims.getSubject() == null) return null;
        
        Account account = accountRepository.getAccountById(claims.getSubject());

        if (account == null || !account.isActive()) return null;

        return new UsernamePasswordAuthenticationToken(new AccountDetails(account), token, AuthorityUtils.NO_AUTHORITIES);
    }

    public Claims parseToken (String token) {
        return this.jwtParser.parseClaimsJws(token).getBody();
    }
}
