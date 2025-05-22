package com.example.database.repository;

import com.example.database.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByShipmentNumber(String shipmentNumber);
} 