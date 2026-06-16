package com.printforge.service;

import com.printforge.model.Product;
import com.printforge.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        sampleProduct.setName("Vase Model A");
        sampleProduct.setMaterial("PLA");
        sampleProduct.setPrice(29.99);
        sampleProduct.setStock(50);
    }

    @Test
    void getById_shouldReturnProduct_whenProductExists() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        // Act
        Product result = productService.getById(1L);

        // Assert
        assertThat(result.getName()).isEqualTo("Vase Model A");
        assertThat(result.getPrice()).isEqualTo(29.99);
    }

    @Test
    void getById_shouldThrowException_whenProductDoesNotExist() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.getById(99L));
    }

    @Test
    void create_shouldSaveAndReturnProduct() {
        // Arrange
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);

        // Act
        Product result = productService.create(sampleProduct);

        // Assert
        assertThat(result.getName()).isEqualTo("Vase Model A");
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        // Act
        productService.delete(1L);

        // Assert
        verify(productRepository, times(1)).deleteById(1L);
    }
}

