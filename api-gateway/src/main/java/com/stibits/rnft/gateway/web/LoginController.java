package com.stibits.rnft.gateway.web;

import java.util.Map;

import javax.validation.Valid;

import com.stibits.rnft.common.api.ApiResponse;
import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.gateway.api.LoginRequest;
import com.stibits.rnft.gateway.errors.InactivatedAccountError;
import com.stibits.rnft.gateway.errors.WrongCredentialsError;
import com.stibits.rnft.gateway.security.jwt.JwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/${app.version}/auth/login")
public class LoginController {
    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping
    public Mono<ResponseEntity<ApiResponse>> login(@Valid @RequestBody LoginRequest request) {
        ResponseEntity<ApiResponse> defaultResponse = new ResponseEntity<>(ApiResponse.getFailureResponse(), HttpStatus.BAD_REQUEST);

        return Mono.create(cb -> {
            try {
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
                )
                .doOnError(LockedException.class, ex -> cb.error(new InactivatedAccountError()))
                .doOnError(AuthenticationException.class, ex -> cb.error(new WrongCredentialsError()))
                .map(authentication -> jwtProvider.createToken(authentication))
                .map(token -> {
                    HttpHeaders headers = new HttpHeaders();
                    ApiResponse body = new ApiSuccessResponse<>(Map.of("access_token", token));

                    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

                    return new ResponseEntity<>(body, headers, HttpStatus.OK);
                })
                .defaultIfEmpty(defaultResponse)
                .doOnSuccess(response -> cb.success(response))
                .subscribe();
            } catch (LockedException ex) {
                cb.error(new InactivatedAccountError());
            } catch (AuthenticationException ex) {
                cb.error(new WrongCredentialsError());
            }
        });
    }
}
