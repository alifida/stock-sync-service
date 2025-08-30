package com.example.stocksync.event;

import com.example.stocksync.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class StockEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public StockEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishStockEvent(StockEventMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.STOCK_EVENT_EXCHANGE,
                RabbitMQConfig.STOCK_EVENT_ROUTING_KEY,
                message
        );
    }
}
