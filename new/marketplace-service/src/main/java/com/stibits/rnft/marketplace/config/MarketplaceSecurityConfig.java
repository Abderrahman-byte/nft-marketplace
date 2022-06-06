package com.stibits.rnft.marketplace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.stibits.rnft.commonweb.config.CommonSecurityConfig;

@Configuration
public class MarketplaceSecurityConfig extends CommonSecurityConfig {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        this.commonConfig(http)
            .authorizeHttpRequests()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .antMatchers(HttpMethod.GET).permitAll()
            .anyRequest().authenticated();
    }
}
