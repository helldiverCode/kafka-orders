package com.example.order.controller;

import com.example.order.dto.OrderEventDTO;
import com.example.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> receiveOrder(@Valid @RequestBody OrderEventDTO orderEvent) {
        String shipmentNumber = orderService.processOrder(orderEvent);
        return ResponseEntity.ok("Order received and being processed. Shipment number: " + shipmentNumber);
    }
} 