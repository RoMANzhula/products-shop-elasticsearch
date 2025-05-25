package org.romanzhula.products_shop_elasticsearch.dto.respons;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;

}
