package com.stibits.rnft.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MarketplaceServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(MarketplaceServiceApp.class, args);
    }    
}
