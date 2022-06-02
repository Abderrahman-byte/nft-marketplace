package com.stibits.rnft.gateway.config;

import com.stibits.rnft.common.utils.MailServiceConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "mail.smtp")
public class MailSmtpPropertiesConfig implements MailServiceConfig {
    private String host;
    private String port;
    private String user;
    private String from;
    private String password;
}
