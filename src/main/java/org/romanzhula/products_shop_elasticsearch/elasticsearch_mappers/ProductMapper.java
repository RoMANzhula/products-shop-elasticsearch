package org.romanzhula.products_shop_elasticsearch.elasticsearch_mappers;

import org.romanzhula.products_shop_elasticsearch.elasticsearch_documents.ProductDocument;
import org.romanzhula.products_shop_elasticsearch.models.Product;

public class ProductMapper {

    public static ProductDocument toDocument(Product product) {
        return ProductDocument.builder()
                .id(product.getId() != null ? product.getId().toString() : null)
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build()
        ;
    }

    public static Product toEntity(ProductDocument document) {
        return Product.builder()
                .id(document.getId() != null ? Long.valueOf(document.getId()) : null)
                .name(document.getName())
                .description(document.getDescription())
                .price(document.getPrice())
                .build()
        ;
    }

}
