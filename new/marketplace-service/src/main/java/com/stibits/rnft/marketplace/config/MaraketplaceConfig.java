package com.stibits.rnft.marketplace.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.stibits.rnft.common.config.CommonConfig;
import com.stibits.rnft.common.helpers.IpfsService;

@Configuration
@EnableWebMvc
@EnableDiscoveryClient
@Import({ CommonConfig.class })
public class MaraketplaceConfig  implements WebMvcConfigurer {
    @Value("#{'${cors.allowOrigins}'.split(',')}")                                  
    private String corsAllowOrigins[]; 

    @Value("${rmq.uri}")
    private String rmqUri;

    @Autowired
    private IpfsProperties ipfsConfig;

    @Bean
    public IpfsService ipfsService () {
        return new IpfsService(ipfsConfig);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(corsAllowOrigins)
            .allowCredentials(true)
            .allowedMethods("*")
            .allowedHeaders("*")
            .maxAge(3600);
    }

    @Bean
    public Connection rmqConnection () throws Exception {
        return rmqConnectionFactory().newConnection();
    }

    @Bean
    public ConnectionFactory rmqConnectionFactory () throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setUri(rmqUri);

        return connectionFactory;
    }
}
