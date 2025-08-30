package com.example.stocksync.vendor.impl;

import java.util.List;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.example.stocksync.model.StockItem;
import com.example.stocksync.vendor.VendorClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("API")
public class ApiVendorClient implements VendorClient {
    @Retryable(value = Exception.class, maxAttempts = 3)
    @Override
    public List<StockItem> fetchStock(String vendorName) {
        log.info("Fetching stock via API for vendor: {}", vendorName);
        try {
            // Simulate Vendor A API response (static data as per document)
            // In a real scenario, you would use RestTemplate or WebClient to call the API
            if ("VendorA".equalsIgnoreCase(vendorName)) {
                return List.of(
                    StockItem.builder()
                        .sku("ABC123")
                        .name("Product A")
                        .stockQuantity(8)
                        .vendor(vendorName)
                        .build(),
                    StockItem.builder()
                        .sku("LMN789")
                        .name("Product C")
                        .stockQuantity(0)
                        .vendor(vendorName)
                        .build()
                );
            }
            return List.of();
        } catch (Exception e) {
            log.error("API fetch failed for vendor {}: {}", vendorName, e.getMessage(), e);
            throw e; // Let @Retryable handle retries
        }
    }
}
