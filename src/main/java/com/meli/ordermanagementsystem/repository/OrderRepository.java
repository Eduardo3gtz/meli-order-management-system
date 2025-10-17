package com.meli.ordermanagementsystem.repository;

import com.meli.ordermanagementsystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Order entity.
 * Extends JpaRepository to get basic CRUD operations for free.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}