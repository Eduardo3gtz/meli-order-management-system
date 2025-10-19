package com.meli.ordermanagementsystem.service;

import com.meli.ordermanagementsystem.model.Order;
import java.util.List;
import java.util.Optional;

/**
 * Interface for the Order Service.
 * Defines the business logic operations for managing orders.
 */
public interface OrderService {

    Order createOrder(Order order);

    List<Order> getAllOrders();

    Optional<Order> getOrderById(Long id);

    Order updateOrder(Long id, Order orderDetails);

    void deleteOrder(Long id);
}