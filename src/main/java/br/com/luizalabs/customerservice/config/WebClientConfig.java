package br.com.luizalabs.customerservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${app-config.url.api}")
    private String apiLuizalabs;

    @Bean
    public WebClient webClient() {
        return WebClient
                .builder()
                .baseUrl(apiLuizalabs)
                .build();
    }

}
