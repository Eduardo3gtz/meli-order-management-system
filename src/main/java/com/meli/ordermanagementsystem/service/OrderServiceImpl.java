package com.meli.ordermanagementsystem.service;

import com.meli.ordermanagementsystem.model.Order;
import com.meli.ordermanagementsystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the OrderService interface.
 * This is where the business logic resides.
 */
@Service // Le dice a Spring que esta es una clase de Servicio
public class OrderServiceImpl implements OrderService {

    // 1. Inyectamos el REPOSITORIO (ya no lo necesita el controlador)
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        order.setOrderDate(LocalDateTime.now()); // La lógica de negocio se queda aquí
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order updateOrder(Long id, Order orderDetails) {
        // Esta es toda la lógica que antes estaba en el controlador
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setCustomerName(orderDetails.getCustomerName());
        order.setStatus(orderDetails.getStatus());
        order.setTotalAmount(orderDetails.getTotalAmount());

        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}