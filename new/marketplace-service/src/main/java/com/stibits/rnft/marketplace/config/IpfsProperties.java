package com.stibits.rnft.marketplace.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.stibits.rnft.common.utils.IpfsServiceConfig;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "ipfs")
public class IpfsProperties implements IpfsServiceConfig {
    private String apiUrl;
    private String apiAuthorization;
    private String gateway;
}

