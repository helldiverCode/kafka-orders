package com.example.demo.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String ORDER_TOPIC = "order-events";

    @Bean
    public NewTopic orderEventsTopic() {
        return TopicBuilder.name(ORDER_TOPIC).partitions(3).replicas(1).build();
    }
}
