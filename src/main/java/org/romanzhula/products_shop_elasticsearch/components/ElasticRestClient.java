package org.romanzhula.products_shop_elasticsearch.components;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@RequiredArgsConstructor
@Component
public class ElasticRestClient {

    private final WebClient webClient;


    public String searchByRest(String queryJson) {
        return webClient.post()
                .uri("/products/_search")
                .header("Content-Type", "application/json")
                .bodyValue(queryJson)
                .retrieve()
                .bodyToMono(String.class)
                .block() // because other services are synch
        ;
    }

}
