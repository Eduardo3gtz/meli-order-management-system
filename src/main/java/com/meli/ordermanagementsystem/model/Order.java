package com.meli.ordermanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank; // Importar
import jakarta.validation.constraints.NotNull;  // Importar
import jakarta.validation.constraints.Positive; // Importar
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Le decimos que este campo no puede ser nulo O estar en blanco
    @NotBlank(message = "Customer name cannot be blank") 
    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @NotBlank(message = "Status cannot be blank")
    @Column(nullable = false)
    private String status;

    @NotNull(message = "Total amount cannot be null")
    @Positive(message = "Total amount must be positive")
    private Double totalAmount;
}