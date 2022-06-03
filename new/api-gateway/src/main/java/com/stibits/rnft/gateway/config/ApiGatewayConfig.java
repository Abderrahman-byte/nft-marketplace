package com.stibits.rnft.gateway.config;

import java.util.function.Function;

import javax.crypto.SecretKey;

import com.stibits.rnft.common.config.CommonConfig;
import com.stibits.rnft.common.helpers.MailService;
import com.stibits.rnft.gateway.security.jwt.JwtGatewayWebFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.jsonwebtoken.security.Keys;

@Configuration
@EnableDiscoveryClient
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

    @Bean
    public RouteLocator routeLocator (RouteLocatorBuilder builder) {
        Function<GatewayFilterSpec, UriSpec> applyFilters = f -> {
            return f.filter(jwtGatewayWebFilter()).stripPrefix(3);
        };

        return builder.routes()
            .route(p -> p.
                path("/api/v1/marketplace/**")
                .filters(applyFilters)
                .uri("lb://marketplace-service"))
            .build();
    }

    @Bean
    public JwtGatewayWebFilter jwtGatewayWebFilter () {
        return new JwtGatewayWebFilter();
    }
}
