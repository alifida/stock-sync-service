package com.example.stocksync.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.stocksync.model.StockItem;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {

     
    @Query("select s from StockItem s where s.sku = :sku and s.vendor = :vendorName")
    Optional<StockItem> findBySkuAndVendor(String sku, String vendorName);
}