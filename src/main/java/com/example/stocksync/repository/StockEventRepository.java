package com.example.stocksync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stocksync.model.StockEvent;

public interface StockEventRepository extends JpaRepository<StockEvent, Long> {
    List<StockEvent> findByVendor(String vendor);
    List<StockEvent> findBySku(String sku);
}
