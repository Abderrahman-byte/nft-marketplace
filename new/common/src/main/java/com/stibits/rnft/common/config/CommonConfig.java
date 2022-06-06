package com.stibits.rnft.common.config;

import com.stibits.rnft.common.clients.ClientsFallback;
import com.stibits.rnft.common.clients.GatewayClient;
import com.stibits.rnft.common.handlers.ApiErrorHandler;
import com.stibits.rnft.common.handlers.MethodArgumentNotValidHandler;
import com.stibits.rnft.common.utils.RandomStringGenerator;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableFeignClients(clients = GatewayClient.class)
public class CommonConfig {
    @Bean
    public RandomStringGenerator randomStringGenerator () {
        return new RandomStringGenerator();
    }    

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MethodArgumentNotValidHandler methodArgumentNotValidHandler () {
        return new MethodArgumentNotValidHandler();
    }

    @Bean
    public ApiErrorHandler apiErrorHandler () {
        return new ApiErrorHandler();
    }

    @Bean
    public ClientsFallback clientsFallback () {
        return new ClientsFallback();
    }
}
