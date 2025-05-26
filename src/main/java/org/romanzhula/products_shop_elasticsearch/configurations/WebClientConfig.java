package org.romanzhula.products_shop_elasticsearch.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Value("${spring.elasticsearch.uris}")
    private String elasticUrl;

    @Bean
    public WebClient elasticWebClient() {
        return WebClient.builder()
                .baseUrl(elasticUrl)
                .build()
        ;
    }

}
