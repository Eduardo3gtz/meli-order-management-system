package com.meli.ordermanagementsystem.controller;

import com.meli.ordermanagementsystem.model.Order;
import com.meli.ordermanagementsystem.service.OrderService; // Importamos el servicio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing orders.
 * Now delegates all business logic to the OrderService.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    // 1. CAMBIAMOS LA INYECCIÓN: Ahora inyectamos el SERVICIO, no el repositorio.
    @Autowired
    private OrderService orderService;
    

    // 2. MÉTODOS MÁS LIMPIOS: Solo llaman al servicio.

   @PostMapping
    public Order createOrder(@Valid @RequestBody Order order) { 
        return orderService.createOrder(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

   @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> orderOptional = orderService.getOrderById(id);

        // Verificamos si la orden fue encontrada
        if (orderOptional.isPresent()) {
            // Si sí, devolvemos la orden con un estado 200 OK
            return ResponseEntity.ok(orderOptional.get());
        } else {
            // Si no, devolvemos un estado 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

   @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @Valid @RequestBody Order orderDetails) { // <--- AÑADIR @Valid
        return orderService.updateOrder(id, orderDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id); // El servicio ya no devuelve un String
        return "Order with ID " + id + " has been deleted successfully."; // El controlador maneja la respuesta
    }
}