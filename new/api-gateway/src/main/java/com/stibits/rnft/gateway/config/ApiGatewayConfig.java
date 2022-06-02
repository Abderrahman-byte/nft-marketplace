package com.stibits.rnft.gateway.config;

import javax.crypto.SecretKey;

import com.stibits.rnft.common.config.CommonConfig;
import com.stibits.rnft.common.helpers.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.jsonwebtoken.security.Keys;

@Configuration
@Import({ CommonConfig.class })
public class ApiGatewayConfig {
    @Value("${jwt.gateway.secret}")
    private String jwtSecret;

    @Autowired
    private MailSmtpPropertiesConfig mailConfig;

    @Bean
    public MailService mailService () {
        return new MailService(mailConfig);
    }

    @Bean
    public SecretKey jwtSecretKey () {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
