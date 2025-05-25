package org.romanzhula.products_shop_elasticsearch.services;

import lombok.RequiredArgsConstructor;
import org.romanzhula.products_shop_elasticsearch.dto.request.ProductRequest;
import org.romanzhula.products_shop_elasticsearch.dto.respons.ProductResponse;
import org.romanzhula.products_shop_elasticsearch.elasticsearch_documents.ProductDocument;
import org.romanzhula.products_shop_elasticsearch.elasticsearch_mappers.ProductMapper;
import org.romanzhula.products_shop_elasticsearch.models.Product;
import org.romanzhula.products_shop_elasticsearch.repositories.elasticsearch_repo.ProductSearchRepository;
import org.romanzhula.products_shop_elasticsearch.repositories.jpa_repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepositoryJpa;
    private final ProductSearchRepository productRepositoryElastic;

    @Transactional
    public ProductResponse save(ProductRequest productRequest) {
        // for jpa
        Product entity = productMapper.toEntityFromProductRequest(productRequest);
        Product savedProduct = productRepositoryJpa.save(entity);

        // for elasticsearch
        ProductDocument document = productMapper.toDocumentFromEntity(savedProduct);
        productRepositoryElastic.save(document);

        return productMapper.toResponseFromEntity(savedProduct);
    }

}
