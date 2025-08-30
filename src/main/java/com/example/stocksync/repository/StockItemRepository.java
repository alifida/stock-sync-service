package com.example.stocksync.repository;

import com.example.stocksync.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {

    Optional<StockItem> findByProductIdAndVendor_Name(String productId, String vendorName);

    @Query("select s from StockItem s where s.productId = :productId and s.vendor.name = :vendorName")
    Optional<StockItem> findByProductAndVendor(String productId, String vendorName);
}