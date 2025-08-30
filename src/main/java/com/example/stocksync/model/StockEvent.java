package com.example.stocksync.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class StockEvent {
    @Id
    @GeneratedValue
    private Long id;

    private String sku;
    private String vendor;
    private Integer oldStock;
    private Integer newStock;
    private String eventType; // OUT_OF_STOCK, RESTOCKED
    private LocalDateTime timestamp;
}
