package com.stibits.rnft.gateway.web;

import com.stibits.rnft.gateway.repository.AccountRepository;
import com.stibits.rnft.gateway.security.jwt.JwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/auth/verify-email")
@Slf4j
public class VerifyEmailController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping
    public String handleGetRequest (@RequestParam(name = "tq", required = false) String token) {
        if (!StringUtils.hasText(token) || !this.jwtProvider.validateToken(token)) 
            return "verify-email-failed";

        try {
            Claims payload = this.jwtProvider.parseToken(token);
            String sub = payload.getSubject();
            String action = payload.get("action", String.class);
            
            if (!StringUtils.hasText(sub) || !StringUtils.hasText(action) || !action.equals("verify-email")) 
                return "verify-email-failed";
                
            boolean verified = accountRepository.activateAccount(sub);

            if (verified) return "verify-email";
        } catch (JwtException ex) {
            log.info("[JwtException] Invalid token " + ex.getMessage());
        }
        
        return "verify-email-failed";
    }
}
