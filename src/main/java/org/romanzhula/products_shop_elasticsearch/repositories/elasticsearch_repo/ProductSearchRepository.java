package org.romanzhula.products_shop_elasticsearch.repositories.elasticsearch_repo;

import org.romanzhula.products_shop_elasticsearch.elasticsearch_documents.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {
}
