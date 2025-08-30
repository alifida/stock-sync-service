package com.example.stocksync.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "stock_items",
    uniqueConstraints = @UniqueConstraint(name = "uk_stockitem_vendor_product", columnNames = {"product_id","vendor_id"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false) // <-- remove unique=true
    private String productId;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;
}
