package com.example.demo.controller;

import com.example.demo.configuration.KafkaConfig;
import com.example.demo.dto.OrderEventDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final KafkaTemplate<String, OrderEventDTO> kafkaTemplate;

    public OrderController(KafkaTemplate<String, OrderEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<String> receiveOrder(@RequestBody OrderEventDTO orderEvent) {
        kafkaTemplate.send(KafkaConfig.ORDER_TOPIC, orderEvent.getShipmentNumber(), orderEvent);
        return ResponseEntity.ok("Order received");
    }
}
