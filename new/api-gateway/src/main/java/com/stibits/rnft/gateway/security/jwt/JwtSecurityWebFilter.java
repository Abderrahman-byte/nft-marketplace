package com.stibits.rnft.gateway.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class JwtSecurityWebFilter implements WebFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = this.resolveToken(request);

        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);

            if (authentication != null)
                return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }
        
        return chain.filter(exchange);
    }

    public String resolveToken (ServerHttpRequest request) {
        String bearer = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)) {
            return bearer.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
