package org.romanzhula.products_shop_elasticsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.romanzhula.products_shop_elasticsearch.dto.request.ProductRequest;
import org.romanzhula.products_shop_elasticsearch.dto.respons.ProductResponse;
import org.romanzhula.products_shop_elasticsearch.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ProductResponse> create(
            @RequestBody ProductRequest request
    ) {
        return ResponseEntity.ok(productService.save(request));
    }

    // Spring Data Elasticsearch example
    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductResponse>> getByName(
            @PathVariable String name
    ) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    // REST API example
    @GetMapping("/search/rest")
    public ResponseEntity<String> searchRest(
            @RequestParam String name
    ) {
        return ResponseEntity.ok(productService.searchByRest(name));
    }

    // Java Client example
    @GetMapping("/search/java")
    public ResponseEntity<List<ProductResponse>> searchJava(
            @RequestParam String name
    ) throws IOException {
        return ResponseEntity.ok(productService.searchByJavaClient(name));
    }

    @PostMapping("/reindex")
    public ResponseEntity<String> reindexAllProducts() {
        productService.reindexAllProducts();
        return ResponseEntity.ok("Reindexing started successfully");
    }

}
