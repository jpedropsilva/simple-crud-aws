package org.jpedro.simplecrudaws.service;

import org.jpedro.simplecrudaws.model.Product;
import org.jpedro.simplecrudaws.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        log.debug("Fetching all products from repository");
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        log.debug("Fetching product by id: {}", id);
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        log.debug("Saving new product: {}", product);
        product.setId(null);
        Product savedProduct = productRepository.save(product);
        log.info("Created product with id: {}", savedProduct.getId());
        return savedProduct;
    }

    public Optional<Product> updateProduct(Long id, Product productDetails) {
        log.debug("Updating product with id: {}", id);
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productDetails.getName());
            existingProduct.setDescription(productDetails.getDescription());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setQuantity(productDetails.getQuantity());
            Product updatedProduct = productRepository.save(existingProduct);
            log.info("Updated product with id: {}", updatedProduct.getId());
            return updatedProduct;
        });
    }

    public boolean deleteProduct(Long id) {
        log.debug("Checking existence of product with id: {} before deletion", id);
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            log.info("Deleted product with id: {}", id);
            return true;
        }
        log.warn("Cannot delete product: id {} does not exist", id);
        return false;
    }
}
