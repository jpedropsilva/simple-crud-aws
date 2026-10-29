package org.jpedro.simplecrudaws.service;

import org.jpedro.simplecrudaws.model.Product;
import org.jpedro.simplecrudaws.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Test Product");
        sampleProduct.setDescription("Description");
        sampleProduct.setPrice(BigDecimal.valueOf(99.99));
        sampleProduct.setQuantity(10);
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(sampleProduct));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById_Found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(2L);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(2L);
    }

    @Test
    void testCreateProduct() {
        Product toCreate = new Product();
        toCreate.setName("New Product");
        toCreate.setPrice(BigDecimal.valueOf(49.99));

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(2L);
            return p;
        });

        Product created = productService.createProduct(toCreate);

        assertNotNull(created);
        assertEquals(2L, created.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_Found() {
        Product updateInfo = new Product();
        updateInfo.setName("Updated Name");
        updateInfo.setDescription("Updated Desc");
        updateInfo.setPrice(BigDecimal.valueOf(120.00));
        updateInfo.setQuantity(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Product> updated = productService.updateProduct(1L, updateInfo);

        assertTrue(updated.isPresent());
        assertEquals("Updated Name", updated.get().getName());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NotFound() {
        Product updateInfo = new Product();
        updateInfo.setName("Updated Name");

        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Product> updated = productService.updateProduct(2L, updateInfo);

        assertFalse(updated.isPresent());
        verify(productRepository, times(1)).findById(2L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_Found() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        boolean result = productService.deleteProduct(1L);

        assertTrue(result);
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(2L)).thenReturn(false);

        boolean result = productService.deleteProduct(2L);

        assertFalse(result);
        verify(productRepository, times(1)).existsById(2L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
