package org.romanzhula.products_shop_elasticsearch.repositories.jpa_repo;

import org.romanzhula.products_shop_elasticsearch.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
