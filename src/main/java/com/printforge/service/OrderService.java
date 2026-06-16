package com.printforge.service;


import com.printforge.model.Order;
import com.printforge.model.Product;
import com.printforge.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public Page<Order> getAll(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);

        if (search != null && !search.isBlank()) {
            return orderRepository.findByCustomerNameContainingIgnoreCase(search, pageable);
        }

        return orderRepository.findAll(pageable);
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public List<Order> getByEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    public Order create(Order order) {
        // Validate product exists and has stock
        Product product = productService.getById(order.getProduct().getId());
        if (product.getStock() < order.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
        // Deduct stock
        product.setStock(product.getStock() - order.getQuantity());
        productService.update(product.getId(), product);

        order.setProduct(product);
        return orderRepository.save(order);
    }

    public Order updateStatus(Long id, String status) {
        Order order = getById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
