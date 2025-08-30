package com.example.stocksync.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.core.QueueBuilder;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String STOCK_EVENT_QUEUE = "stock.events.queue";
    public static final String STOCK_EVENT_EXCHANGE = "stock.events.exchange";
    public static final String STOCK_EVENT_ROUTING_KEY = "stock.events.key";

    @Bean
    public Queue stockEventQueue() {
        return QueueBuilder.durable(STOCK_EVENT_QUEUE).build();
    }

    @Bean
    public TopicExchange stockEventExchange() {
        return new TopicExchange(STOCK_EVENT_EXCHANGE);
    }

    @Bean
    public Binding stockEventBinding() {
        return BindingBuilder
                .bind(stockEventQueue())
                .to(stockEventExchange())
                .with(STOCK_EVENT_ROUTING_KEY);
    }
    
    
    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
    
}
