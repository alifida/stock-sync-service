package com.example.stocksync.event;

import com.example.stocksync.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class StockEventListener {

    @RabbitListener(queues = RabbitMQConfig.STOCK_EVENT_QUEUE)
    public void handleStockEvent(StockEventMessage message) {
        // Placeholder: Add processing logic (e.g., email alerts, metrics, etc.)
        System.out.println("Received stock event: " + message.getProductId() +
                " vendor: " + message.getVendor() +
                " stock changed from " + message.getOldStock() +
                " to " + message.getNewStock());
    }
}
