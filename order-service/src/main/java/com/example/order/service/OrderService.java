package com.example.order.service;

import com.example.order.dto.OrderEventDTO;
import com.example.order.config.KafkaConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.UUID;

@Service
@Validated
public class OrderService {

    private final KafkaTemplate<String, OrderEventDTO> kafkaTemplate;

    public OrderService(KafkaTemplate<String, OrderEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String processOrder(@Valid OrderEventDTO orderEvent) {
        // Generate shipment number if not provided
        if (orderEvent.getShipmentNumber() == null || orderEvent.getShipmentNumber().isEmpty()) {
            orderEvent.setShipmentNumber(generateShipmentNumber());
        }

        // Validate country codes (simple validation example)
        validateCountryCodes(orderEvent);

        // Publish to Kafka for further processing
        kafkaTemplate.send(KafkaConfig.ORDER_TOPIC, orderEvent.getShipmentNumber(), orderEvent);

        // Return the shipment number for tracking
        return orderEvent.getShipmentNumber();
    }

    private String generateShipmentNumber() {
        return "SHP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void validateCountryCodes(OrderEventDTO orderEvent) {
        String receiverCountry = orderEvent.getReceiverCountryCode();
        String senderCountry = orderEvent.getSenderCountryCode();

        if (receiverCountry == null || receiverCountry.length() != 2) {
            throw new IllegalArgumentException("Receiver country code must be a 2-letter ISO code");
        }

        if (senderCountry == null || senderCountry.length() != 2) {
            throw new IllegalArgumentException("Sender country code must be a 2-letter ISO code");
        }
    }
} 