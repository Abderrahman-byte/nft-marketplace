package com.stibits.rnft.marketplace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.stibits.rnft.common.config.CommonConfig;
import com.stibits.rnft.commonweb.config.CommonSecurityConfig;

@Configuration
@Import({ CommonConfig.class })
public class MarketplaceConfig extends CommonSecurityConfig {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        this.commonConfig(http).authorizeHttpRequests().anyRequest().authenticated();
    }
}
