package com.example.stocksync.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stocksync.model.StockRecord;

public interface StockRepository extends JpaRepository<StockRecord, Long> {
	Optional<StockRecord> findByVendorNameAndProductId(String vendorName, String productId);
}
