package org.jpedro.simplecrudaws.controller;

import org.jpedro.simplecrudaws.model.Product;
import org.jpedro.simplecrudaws.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        log.info("Received request to get all products");
        List<Product> products = productService.getAllProducts();
        log.info("Returning {} products", products.size());
        return products;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.info("Received request to get product by id: {}", id);
        return productService.getProductById(id)
                .map(product -> {
                    log.info("Product found with id: {}", id);
                    return ResponseEntity.ok(product);
                })
                .orElseGet(() -> {
                    log.warn("Product not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        log.info("Received request to create product: {}", product);
        Product createdProduct = productService.createProduct(product);
        log.info("Product created successfully with id: {}", createdProduct.getId());
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        log.info("Received request to update product with id: {}", id);
        return productService.updateProduct(id, product)
                .map(updatedProduct -> {
                    log.info("Product updated successfully with id: {}", id);
                    return ResponseEntity.ok(updatedProduct);
                })
                .orElseGet(() -> {
                    log.warn("Product not found for update with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Received request to delete product with id: {}", id);
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            log.info("Product deleted successfully with id: {}", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("Product not found for deletion with id: {}", id);
        return ResponseEntity.notFound().build();
    }
}
