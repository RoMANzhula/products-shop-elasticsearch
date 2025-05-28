package org.romanzhula.products_shop_elasticsearch.events;

import lombok.Getter;
import org.romanzhula.products_shop_elasticsearch.models.Product;


@Getter
public class ProductSavedEvent {

    private final Product product;


    public ProductSavedEvent(Object source, Product product) {
        this.product = product;
    }

}
