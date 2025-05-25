package org.romanzhula.products_shop_elasticsearch.dto.request;

import lombok.Data;

@Data
public class ProductRequest {

    private String name;
    private String description;
    private Double price;

}
