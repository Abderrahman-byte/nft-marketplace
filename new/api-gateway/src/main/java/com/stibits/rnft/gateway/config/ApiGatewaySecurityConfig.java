package com.stibits.rnft.gateway.config;

import com.stibits.rnft.gateway.security.jwt.JwtSecurityWebFilter;
import com.stibits.rnft.gateway.services.AccountDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class ApiGatewaySecurityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountDetailsService accountDetailsService;

    @Autowired
    private JwtSecurityWebFilter jwtSecurityWebFilter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain (ServerHttpSecurity http) {
        http
            .csrf().disable()
            .logout().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .addFilterAt(jwtSecurityWebFilter, SecurityWebFiltersOrder.HTTP_BASIC)
            .authenticationManager(authenticationManager())
            .authorizeExchange()
            .pathMatchers(HttpMethod.PUT, "/api/*/auth/profile").authenticated()
            .pathMatchers(
                "/api/*/auth/**", 
                "/auth/**", 
                "/api/*/marketplace/**", 
                "/api/*/users/**"
            ).permitAll()
            .anyExchange().authenticated();

        return http.build();
    }


    @Bean
    public ReactiveAuthenticationManager authenticationManager () {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
            new UserDetailsRepositoryReactiveAuthenticationManager(accountDetailsService);

        authenticationManager.setPasswordEncoder(passwordEncoder);

        return authenticationManager;
    }
}
