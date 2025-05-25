package org.romanzhula.products_shop_elasticsearch.elasticsearch_mappers;

import org.romanzhula.products_shop_elasticsearch.dto.request.ProductRequest;
import org.romanzhula.products_shop_elasticsearch.dto.respons.ProductResponse;
import org.romanzhula.products_shop_elasticsearch.elasticsearch_documents.ProductDocument;
import org.romanzhula.products_shop_elasticsearch.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDocument toDocumentFromEntity(Product product) {
        return ProductDocument.builder()
                .id(product.getId() != null ? product.getId().toString() : null)
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build()
        ;
    }

    public Product toEntityFromProductRequest(ProductRequest document) {
        return Product.builder()
                .name(document.getName())
                .description(document.getDescription())
                .price(document.getPrice())
                .build()
        ;
    }

    public ProductResponse toResponseFromEntity(Product entity) {
        return ProductResponse.builder()
                .id(entity.getId() != null ? entity.getId() : null)
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .build()
        ;
    }

    public ProductResponse toResponseFromDocument(ProductDocument doc) {
        return ProductResponse.builder()
                .id(doc.getId() != null ? Long.valueOf(doc.getId()) : null)
                .name(doc.getName())
                .description(doc.getDescription())
                .price(doc.getPrice())
                .build()
        ;
    }

}
