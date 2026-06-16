package com.printforge.service;

import com.printforge.model.Order;
import com.printforge.model.Product;
import com.printforge.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private Product sampleProduct;
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Vase Model A");
        sampleProduct.setStock(10);
        sampleProduct.setPrice(29.99);

        sampleOrder = new Order();
        sampleOrder.setProduct(sampleProduct);
        sampleOrder.setQuantity(2);
        sampleOrder.setCustomerName("Jane Doe");
        sampleOrder.setCustomerEmail("jane@test.com");
    }

    @Test
    void create_shouldSaveOrder_whenStockIsSufficient() {
        // Arrange
        when(productService.getById(1L)).thenReturn(sampleProduct);
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        // Act
        Order result = orderService.create(sampleOrder);

        // Assert
        assertThat(result.getCustomerName()).isEqualTo("Jane Doe");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void create_shouldThrowException_whenStockIsInsufficient() {
        // Arrange
        sampleProduct.setStock(1);   // only 1 in stock
        sampleOrder.setQuantity(5);  // but customer wants 5

        when(productService.getById(1L)).thenReturn(sampleProduct);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.create(sampleOrder));

        assertThat(exception.getMessage()).contains("Insufficient stock");
    }

    @Test
    void create_shouldReduceProductStock_whenOrderPlaced() {
        // Arrange
        when(productService.getById(1L)).thenReturn(sampleProduct);
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        // Act
        orderService.create(sampleOrder);

        // Assert
        assertThat(sampleProduct.getStock()).isEqualTo(8);   // 10 - 2 = 8
        verify(productService, times(1)).update(eq(1L), any(Product.class));
    }

    @Test
    void updateStatus_shouldChangeOrderStatus() {
        // Arrange
        sampleOrder.setId(1L);
        sampleOrder.setStatus("PENDING");
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(sampleOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        // Act
        Order result = orderService.updateStatus(1L, "COMPLETED");

        // Assert
        assertThat(result.getStatus()).isEqualTo("COMPLETED");
    }
}