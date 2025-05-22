package com.example.order.controller;

import com.example.dto.OrderEventDTO;
import com.example.order.config.KafkaConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final KafkaTemplate<String, OrderEventDTO> kafkaTemplate;

    public OrderController(KafkaTemplate<String, OrderEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<String> receiveOrder(@Valid @RequestBody OrderEventDTO orderEvent) {
        // Send to Kafka using shipment number as the key for partitioning
        kafkaTemplate.send(KafkaConfig.ORDER_TOPIC, orderEvent.getShipmentNumber(), orderEvent);
        return ResponseEntity.ok("Order received and being processed");
    }
} 