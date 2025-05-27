package com.example.email.consumer;

import com.example.email.dto.OrderEventDTO;
import com.example.email.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private final EmailService emailService;

    public OrderEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "order-events", groupId = "email-service-group")
    public void handleOrderEvent(OrderEventDTO orderEvent) {
        String subject = "Order Update - Shipment " + orderEvent.getShipmentNumber();
        String content = String.format(
            "Your order with shipment number %s has been updated. Current status: %d",
            orderEvent.getShipmentNumber(),
            orderEvent.getStatusCode()
        );

        emailService.sendEmail(orderEvent.getReceiverEmail(), subject, content);
    }
} 