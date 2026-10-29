package org.jpedro.simplecrudaws.controller;

import org.jpedro.simplecrudaws.model.Product;
import org.jpedro.simplecrudaws.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Sample Product");
        sampleProduct.setDescription("Sample Description");
        sampleProduct.setPrice(BigDecimal.valueOf(19.99));
        sampleProduct.setQuantity(5);
    }

    @Test
    void testGetAllProducts() {
        when(productService.getAllProducts()).thenReturn(List.of(sampleProduct));

        List<Product> result = productController.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sample Product", result.get(0).getName());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById_Found() {
        when(productService.getProductById(1L)).thenReturn(Optional.of(sampleProduct));

        ResponseEntity<Product> response = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productService.getProductById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProductById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService, times(1)).getProductById(999L);
    }

    @Test
    void testCreateProduct() {
        when(productService.createProduct(any(Product.class))).thenReturn(sampleProduct);

        ResponseEntity<Product> response = productController.createProduct(sampleProduct);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    void testUpdateProduct_Found() {
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(Optional.of(sampleProduct));

        ResponseEntity<Product> response = productController.updateProduct(1L, sampleProduct);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productService.updateProduct(eq(999L), any(Product.class))).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.updateProduct(999L, sampleProduct);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService, times(1)).updateProduct(eq(999L), any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() {
        when(productService.deleteProduct(1L)).thenReturn(true);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productService.deleteProduct(999L)).thenReturn(false);

        ResponseEntity<Void> response = productController.deleteProduct(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(999L);
    }
}
