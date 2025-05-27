package com.example.logging.consumer;

import com.example.logging.dto.OrderEventDTO;
import com.example.logging.entity.OrderLog;
import com.example.logging.repository.OrderLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final OrderLogRepository orderLogRepository;

    public OrderEventConsumer(OrderLogRepository orderLogRepository) {
        this.orderLogRepository = orderLogRepository;
    }

    @Transactional
    @KafkaListener(
        topics = "order-events",
        groupId = "logging-service-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrderEvents(List<OrderEventDTO> orderEvents) {
        logger.info("Received batch of {} order events", orderEvents.size());
        
        try {
            List<OrderLog> orderLogs = orderEvents.stream()
                .map(this::convertToOrderLog)
                .collect(Collectors.toList());
            
            orderLogRepository.saveAll(orderLogs);
            logger.info("Successfully processed {} order events", orderEvents.size());
            
        } catch (Exception e) {
            logger.error("Error processing order events batch: {}", e.getMessage(), e);
            throw e; // Let the retry policy handle it
        }
    }

    private OrderLog convertToOrderLog(OrderEventDTO orderEvent) {
        OrderLog orderLog = new OrderLog();
        orderLog.setShipmentNumber(orderEvent.getShipmentNumber());
        orderLog.setReceiverEmail(orderEvent.getReceiverEmail());
        orderLog.setReceiverCountryCode(orderEvent.getReceiverCountryCode());
        orderLog.setSenderCountryCode(orderEvent.getSenderCountryCode());
        orderLog.setStatusCode(orderEvent.getStatusCode());
        orderLog.setLoggedAt(LocalDateTime.now());
        return orderLog;
    }
} 