package com.example.stocksync.repository;

import com.example.stocksync.model.StockEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockEventRepository extends JpaRepository<StockEvent, Long> {
    List<StockEvent> findByVendor_Name(String vendorName);
    List<StockEvent> findByProductId(String productId);
}
