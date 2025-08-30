package com.example.stocksync.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "stock_items",
    uniqueConstraints = @UniqueConstraint(name = "uk_stockitem_vendor_product", columnNames = {"sku","vendor"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockItem {
    @Id
    @GeneratedValue
    private UUID id;

    private String sku;
    private String name;
    private Integer stockQuantity;
    private String vendor;
    private LocalDateTime lastUpdated;
}
