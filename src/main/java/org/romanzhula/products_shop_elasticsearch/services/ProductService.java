package org.romanzhula.products_shop_elasticsearch.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import org.romanzhula.products_shop_elasticsearch.components.ElasticRestClient;
import org.romanzhula.products_shop_elasticsearch.dto.request.ProductRequest;
import org.romanzhula.products_shop_elasticsearch.dto.respons.ProductResponse;
import org.romanzhula.products_shop_elasticsearch.elasticsearch_documents.ProductDocument;
import org.romanzhula.products_shop_elasticsearch.elasticsearch_mappers.ProductMapper;
import org.romanzhula.products_shop_elasticsearch.models.Product;
import org.romanzhula.products_shop_elasticsearch.repositories.elasticsearch_repo.ProductSearchRepository;
import org.romanzhula.products_shop_elasticsearch.repositories.jpa_repo.ProductRepository;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepositoryJpa;
    private final ProductSearchRepository productRepositoryElastic;
    private final ElasticRestClient elasticRestClient;
    private final ElasticsearchClient elasticsearchClient;


    @Transactional
    public ProductResponse save(ProductRequest productRequest) {
        // for jpa
        Product entity = productMapper.toEntityFromProductRequest(productRequest);
        Product savedProduct = productRepositoryJpa.save(entity);

        // for elasticsearch
        try {
            ProductDocument document = productMapper.toDocumentFromEntity(savedProduct);
            productRepositoryElastic.save(document);
        } catch (Exception e) {
            throw new RuntimeException("Failed to index in Elasticsearch", e);
        }

        return productMapper.toResponseFromEntity(savedProduct);
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

}
