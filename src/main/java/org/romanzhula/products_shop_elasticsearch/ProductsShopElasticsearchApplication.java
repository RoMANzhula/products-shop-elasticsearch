package org.romanzhula.products_shop_elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.romanzhula.products_shop_elasticsearch")
public class ProductsShopElasticsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsShopElasticsearchApplication.class, args);
	}

}
