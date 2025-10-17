package com.meli.ordermanagementsystem.controller;

import com.meli.ordermanagementsystem.model.Order;
import com.meli.ordermanagementsystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing orders.
 * Exposes endpoints for CRUD operations on orders.
 */
@RestController
@RequestMapping("/api/orders") // Base URL for all endpoints in this controller
public class OrderController {

    @Autowired // Spring's magic to inject the OrderRepository instance
    private OrderRepository orderRepository;

    // CREATE a new order
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        order.setOrderDate(LocalDateTime.now()); // Set the order date on the server
        return orderRepository.save(order);
    }

    // READ all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // READ a single order by ID
    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id);
    }

    // UPDATE an existing order
    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setCustomerName(orderDetails.getCustomerName());
        order.setStatus(orderDetails.getStatus());
        order.setTotalAmount(orderDetails.getTotalAmount());
        // The order date is not updated intentionally

        return orderRepository.save(order);
    }

    // DELETE an order
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return "Order with ID " + id + " has been deleted successfully.";
    }
}