package com.example.stocksync.service;

import com.example.stocksync.event.StockEventMessage;
import com.example.stocksync.event.StockEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StockSyncService {

    private final StockEventPublisher eventPublisher;

    // Simulating a local database for now
    private final Map<String, Integer> productStockDb = new HashMap<>();

    public StockSyncService(StockEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;

        // Pre-populate with some test data
        productStockDb.put("P1001", 10);
        productStockDb.put("P1002", 5);
        productStockDb.put("P1003", 0);
    }

    /**
     * Syncs stock for a single product and fires an event if it changed.
     */
    public void syncStock(String productId, int newStock, String vendor) {
        int oldStock = productStockDb.getOrDefault(productId, 0);

        if (oldStock != newStock) {
            // Update local stock
            productStockDb.put(productId, newStock);

            // Publish event
            StockEventMessage event = new StockEventMessage(
                    productId, vendor, oldStock, newStock
            );
            eventPublisher.publishStockEvent(event);

            System.out.println("Stock updated & event published: " + event.getProductId());
        } else {
            System.out.println("No stock change for " + productId);
        }
    }

    /**
     * Mock method to simulate syncing from a vendor API.
     */
    public void syncFromVendor(String vendor) {
        // In real life, you'd call an API here
        System.out.println("Syncing stock for vendor: " + vendor);

        // Example updates
        syncStock("P1001", 8, vendor); // Changed
        syncStock("P1002", 5, vendor); // No change
        syncStock("P1003", 2, vendor); // Changed
    }
}
