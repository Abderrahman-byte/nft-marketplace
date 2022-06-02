package com.stibits.rnft.gateway.web;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.stibits.rnft.common.api.ApiResponse;
import com.stibits.rnft.common.errors.ApiError;
import com.stibits.rnft.common.errors.InvalidFieldsError;
import com.stibits.rnft.common.helpers.MailService;
import com.stibits.rnft.gateway.api.RegisterRequest;
import com.stibits.rnft.gateway.domain.Account;
import com.stibits.rnft.gateway.errors.EmailExistsError;
import com.stibits.rnft.gateway.errors.UsernameExistsError;
import com.stibits.rnft.gateway.repository.AccountRepository;
import com.stibits.rnft.gateway.security.jwt.JwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/${app.version}/auth/register")
@Slf4j
public class RegisterController {
    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping
    public Mono<ApiResponse> register (ServerHttpRequest httpRequest, @Valid @RequestBody RegisterRequest request) {
        return Mono.create(cb -> {
            if (!request.getPassword().equals(request.getPassword2())) {
                cb.error(new InvalidFieldsError(List.of(
                    new InvalidFieldsError.InvalidField("password2", "Passwords dont match.")
                )));
            }
    
            try {
                Account account = accountRepository.insertAccount(request.getUsername(), request.getEmail(), request.getPassword());
    
                cb.success(ApiResponse.getSuccessResponse());
                this.sendActivationMail(account, httpRequest);
            } catch (DataIntegrityViolationException ex) {
                ApiError error = ex.getMessage().contains("unique_email") ? new EmailExistsError() : new UsernameExistsError();
                cb.error(error);
            }
        });
    }

    private void sendActivationMail (Account account, ServerHttpRequest request) {
        Context ctx = new Context();

        ctx.setVariable("account", account);
        ctx.setVariable("verificationUrl", this.generateVerificationUrl(account, request));

        String content = springTemplateEngine.process("email-verification", ctx);
        String subject = "Stibits RNFT Email Verification";
        String contentType = "text/html; charset=utf-8";

        mailService.sendMail(account.getEmail(), subject , content, contentType);
    }

    private String generateVerificationUrl (Account account, ServerHttpRequest request) {
        return UriComponentsBuilder
            .fromHttpRequest(request)
            .replacePath("/auth/verify-email")
            .queryParam("tq", this.generateVerificationToken(account))
            .build()
            .toUriString();
    }

    private String generateVerificationToken (Account account) {
        try {
            Map<String, Object> claims = Map.of("action", "verify-email");
            Calendar expires = Calendar.getInstance();

            expires.add(Calendar.MONTH, 1);

            return jwtProvider.createToken(account.getId(), claims, expires);
        } catch (JwtException ex) {
            log.error("[JwtException] " + ex.getMessage(), ex);
            return "";
        }
    }
}
