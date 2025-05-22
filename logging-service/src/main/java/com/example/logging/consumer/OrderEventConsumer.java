package com.example.logging.consumer;

import com.example.dto.OrderEventDTO;
import com.example.logging.entity.OrderLog;
import com.example.logging.repository.OrderLogRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderEventConsumer {

    private final OrderLogRepository orderLogRepository;

    public OrderEventConsumer(OrderLogRepository orderLogRepository) {
        this.orderLogRepository = orderLogRepository;
    }

    @KafkaListener(topics = "order-events", groupId = "logging-service-group")
    public void handleOrderEvent(OrderEventDTO orderEvent) {
        OrderLog orderLog = new OrderLog();
        orderLog.setShipmentNumber(orderEvent.getShipmentNumber());
        orderLog.setReceiverEmail(orderEvent.getReceiverEmail());
        orderLog.setReceiverCountryCode(orderEvent.getReceiverCountryCode());
        orderLog.setSenderCountryCode(orderEvent.getSenderCountryCode());
        orderLog.setStatusCode(orderEvent.getStatusCode());
        orderLog.setLoggedAt(LocalDateTime.now());
        
        orderLogRepository.save(orderLog);
    }
} 