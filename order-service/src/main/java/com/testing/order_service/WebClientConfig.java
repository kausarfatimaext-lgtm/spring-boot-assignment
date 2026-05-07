package com.testing.order_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig{
    @Bean
    public WebClient productClient(){
        return WebClient.builder()
                .baseUrl("http://product-service:8081")
                .build();
    }

    @Bean
    public WebClient notificationClient(){
        return WebClient.builder()
                .baseUrl("http://notification-service:8083")
                .build();
    }
}
