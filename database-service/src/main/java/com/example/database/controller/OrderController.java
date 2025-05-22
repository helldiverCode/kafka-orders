package com.example.database.controller;

import com.example.database.entity.Order;
import com.example.database.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{shipmentNumber}")
    public ResponseEntity<Order> getOrderByShipmentNumber(@PathVariable String shipmentNumber) {
        Order order = orderRepository.findByShipmentNumber(shipmentNumber);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @PutMapping("/{shipmentNumber}")
    public ResponseEntity<Order> updateOrder(@PathVariable String shipmentNumber, @RequestBody Order orderDetails) {
        Order order = orderRepository.findByShipmentNumber(shipmentNumber);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        order.setStatusCode(orderDetails.getStatusCode());
        order.setUpdatedAt(LocalDateTime.now());
        
        Order updatedOrder = orderRepository.save(order);
        return ResponseEntity.ok(updatedOrder);
    }
} 