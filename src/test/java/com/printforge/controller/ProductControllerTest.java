package com.printforge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.printforge.model.Product;
import com.printforge.security.JwtUtil;
import com.printforge.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAll_shouldReturnPageOfProducts() throws Exception {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Vase Model A");
        product.setMaterial("PLA");
        product.setPrice(29.99);
        product.setStock(50);

        Page<Product> page = new PageImpl<>(List.of(product));
        when(productService.getAll(0, 10, null)).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/products?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Vase Model A"));
    }

    @Test
    @WithMockUser
    void create_shouldReturn400_whenNameIsBlank() throws Exception {
        // Arrange
        Product invalidProduct = new Product();
        invalidProduct.setName("");          // blank — should fail validation
        invalidProduct.setMaterial("PLA");
        invalidProduct.setPrice(10.0);
        invalidProduct.setStock(5);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @WithMockUser
    void create_shouldReturn200_whenProductIsValid() throws Exception {
        // Arrange
        Product validProduct = new Product();
        validProduct.setName("Phone Stand");
        validProduct.setMaterial("PETG");
        validProduct.setPrice(14.99);
        validProduct.setStock(100);

        when(productService.create(any(Product.class))).thenReturn(validProduct);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Phone Stand"));
    }
}