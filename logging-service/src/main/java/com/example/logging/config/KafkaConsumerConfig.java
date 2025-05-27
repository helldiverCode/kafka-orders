package com.example.logging.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;

@Configuration
public class KafkaConsumerConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory) {
        
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        
        // Set concurrency (number of consumer threads)
        factory.setConcurrency(3);
        
        // Configure batch listening
        factory.setBatchListener(true);
        
        // Configure container properties
        ContainerProperties containerProperties = factory.getContainerProperties();
        containerProperties.setPollTimeout(3000);
        containerProperties.setIdleEventInterval(60000L);
        
        // Configure error handler with exponential backoff
        ExponentialBackOff exponentialBackOff = new ExponentialBackOff(
            1000L, // initial interval
            2.0    // multiplier
        );
        exponentialBackOff.setMaxInterval(10000L); // max interval
        exponentialBackOff.setMaxElapsedTime(30000L); // max elapsed time
        
        // Create error handler with fixed number of retry attempts (3)
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
            (record, exception) -> {
                // Log the failed record
                logger.error("Failed to process record: {}. Error: {}", record, exception.getMessage(), exception);
            },
            exponentialBackOff
        );
        
        // Configure which exceptions should be retried
        errorHandler.addRetryableExceptions(
            org.springframework.kafka.listener.ListenerExecutionFailedException.class,
            org.springframework.messaging.MessagingException.class
        );
        
        factory.setCommonErrorHandler(errorHandler);
        
        return factory;
    }
} 