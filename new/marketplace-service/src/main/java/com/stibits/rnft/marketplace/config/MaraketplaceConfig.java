package com.stibits.rnft.marketplace.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.stibits.rnft.common.config.CommonConfig;

@Configuration
@EnableWebMvc
@EnableDiscoveryClient
@Import({ CommonConfig.class })
public class MaraketplaceConfig  implements WebMvcConfigurer {
    @Value("#{'${cors.allowOrigins}'.split(',')}")                                  
    String corsAllowOrigins[]; 

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(corsAllowOrigins)
            .allowCredentials(true)
            .allowedMethods("*")
            .allowedHeaders("*")
            .maxAge(3600);
    }
}
