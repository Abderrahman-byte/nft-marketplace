package com.stibits.rnft.gateway.config;

                                          
import java.util.Arrays;
                                          
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

@Configuration
public class ApiGatewayCorsConfig extends CorsConfiguration {
    @Value("#{'${cors.allowOrigins}'.split(',')}")                                  
    private String corsAllowOrigins[]; 

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        if (corsAllowOrigins == null || corsAllowOrigins.length <= 0) corsConfiguration.addAllowedOrigin("*");
        else for (String origin: corsAllowOrigins) corsConfiguration.addAllowedOrigin(origin);
        
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        corsConfiguration.addAllowedHeader("origin");
        corsConfiguration.addAllowedHeader("content-type");
        corsConfiguration.addAllowedHeader("accept");
        corsConfiguration.addAllowedHeader("authorization");
        corsConfiguration.addAllowedHeader("cookie");
        source.registerCorsConfiguration("/api/**", corsConfiguration);

        return new CorsWebFilter(source);
    }

}
