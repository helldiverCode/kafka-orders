package com.example.demo.consumer;

import com.example.demo.configuration.KafkaConfig;
import com.example.demo.dao.OrderRepository;
import com.example.demo.dto.OrderEventDTO;
import com.example.demo.entity.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderEventConsumer {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderEventConsumer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = KafkaConfig.ORDER_TOPIC, groupId = "order-group")
    public void handleOrder(OrderEventDTO dto) {
        OrderEvent order = new OrderEvent();
        order.setShipmentNumber(dto.getShipmentNumber());
        order.setReceiverEmail(dto.getReceiverEmail());
        order.setReceiverCountryCode(dto.getReceiverCountryCode());
        order.setSenderCountryCode(dto.getSenderCountryCode());
        order.setReceivedAt(LocalDateTime.now());
        orderRepository.save(order);

        System.out.println("Mock e-mail sent to: " + dto.getReceiverEmail());
    }
}

