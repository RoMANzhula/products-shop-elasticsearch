package org.romanzhula.products_shop_elasticsearch.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.romanzhula.products_shop_elasticsearch.components.ElasticRestClient;
import org.romanzhula.products_shop_elasticsearch.dto.request.ProductRequest;
import org.romanzhula.products_shop_elasticsearch.dto.respons.ProductResponse;
import org.romanzhula.products_shop_elasticsearch.elasticsearch_documents.ProductDocument;
import org.romanzhula.products_shop_elasticsearch.elasticsearch_mappers.ProductMapper;
import org.romanzhula.products_shop_elasticsearch.events.ProductSavedEvent;
import org.romanzhula.products_shop_elasticsearch.models.Product;
import org.romanzhula.products_shop_elasticsearch.repositories.elasticsearch_repo.ProductSearchRepository;
import org.romanzhula.products_shop_elasticsearch.repositories.jpa_repo.ProductRepository;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepositoryJpa;
    private final ProductSearchRepository productRepositoryElastic;
    private final ElasticRestClient elasticRestClient;
    private final ElasticsearchClient elasticsearchClient;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public ProductResponse save(ProductRequest productRequest) {
        // for jpa
        Product entity = productMapper.toEntityFromProductRequest(productRequest);
        Product savedProduct = productRepositoryJpa.save(entity);

        // after commited jpa transaction for elasticsearch
        eventPublisher.publishEvent(new ProductSavedEvent(this, savedProduct));

        return productMapper.toResponseFromEntity(savedProduct);
    }

    // Handle event after commit
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductSavedEvent(ProductSavedEvent event) {
        Product savedProduct = event.getProduct();

        try {
            ProductDocument document = productMapper.toDocumentFromEntity(savedProduct);
            productRepositoryElastic.save(document);
            log.info("Product indexed in Elasticsearch: id={}", savedProduct.getId());
        } catch (Exception e) {
            log.error("Failed to index product in Elasticsearch: id={}", savedProduct.getId(), e);
        }
    }

    //  Spring Data Elasticsearch example
    public List<ProductResponse> findByName(String name) {
        return productRepositoryElastic.findByName(name).stream()
                .map(productMapper::toResponseFromDocument)
                .collect(Collectors.toList()
                );
    }

    // Rest API example
    public String searchByRest(String keyword) {
        String queryJson = """
                {
                  "query": {
                    "match": {
                      "name": "%s"
                    }
                  }
                }
                """.formatted(keyword)
        ;

        return elasticRestClient.searchByRest(queryJson);
    }

    // Java Client example
    public List<ProductResponse> searchByJavaClient(String name) throws IOException {
        SearchResponse<ProductDocument> response = elasticsearchClient.search(s -> s
                        .index("products")
                        .query(q -> q
                                .match(m -> m
                                        .field("name")
                                        .query(name)
                                )
                        ),
                ProductDocument.class
        );

        return response.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .map(productMapper::toResponseFromDocument)
                .collect(Collectors.toList())
        ;
    }

    public List<ProductResponse> getAllIndexedProducts() {
        Iterable<ProductDocument> documents = productRepositoryElastic.findAll();
        return StreamSupport.stream(documents.spliterator(), false)
                .map(productMapper::toResponseFromDocument)
                .collect(Collectors.toList());
    }

}
