package com.example.stocksync.service;

import com.example.stocksync.model.Vendor;
import com.example.stocksync.event.StockEventMessage;
import com.example.stocksync.event.StockEventPublisher;
import com.example.stocksync.model.StockEvent;
import com.example.stocksync.model.StockItem;
import com.example.stocksync.repository.StockEventRepository;
import com.example.stocksync.repository.StockItemRepository;
import com.example.stocksync.repository.VendorRepository;
import com.example.stocksync.vendor.VendorClient;
import com.example.stocksync.vendor.VendorClientFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockSyncService {

    private final VendorClientFactory vendorClientFactory;
    private final VendorRepository vendorRepository;
    private final StockItemRepository stockItemRepository;
    private final StockEventRepository stockEventRepository;
    private final StockEventPublisher eventPublisher;

    /**
     * Sync all stock items for a vendor (Delta-based).
     */
    @Transactional
    public void syncStockForVendor(String vendorName) {
        log.info("Starting stock sync for vendor: {}", vendorName);

        Vendor vendor = vendorRepository.findByName(vendorName)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found: " + vendorName));

        VendorClient client = vendorClientFactory.getClient(vendorName);
        List<StockItem> stockItems = client.fetchStock(vendorName);

        stockItems.forEach(item -> processStockDelta(vendor, item));

        log.info("Completed stock sync for vendor: {}", vendorName);
    }

    /**
     * Handles stock changes (delta detection) for a single product.
     */
    private void processStockDelta(Vendor vendor, StockItem item) {
        StockItem stockItem = stockItemRepository
                .findByProductIdAndVendor_Name(item.getProductId(), vendor.getName())
                .orElse(StockItem.builder()
                        .productId(item.getProductId())
                        .vendor(vendor)
                        .stock(0)
                        .build());

        int oldStock = stockItem.getStock();
        int newStock = item.getStock();

        if (oldStock != newStock) {
            stockItem.setStock(newStock);
            stockItemRepository.save(stockItem);
            saveAndPublishEvent(item.getProductId(), vendor, oldStock, newStock);
            log.info("Stock updated for product {}: {} -> {}", item.getProductId(), oldStock, newStock);
        } else {
            log.debug("No stock change for product {}", item.getProductId());
        }
    }

    /**
     * Saves a StockEvent in the database and publishes it to the message queue.
     */
    private void saveAndPublishEvent(String productId, Vendor vendor, int oldStock, int newStock) {
        StockEvent stockEvent = StockEvent.builder()
                .productId(productId)
                .vendor(vendor)
                .oldStock(oldStock)
                .newStock(newStock)
                .build();
        stockEventRepository.save(stockEvent);

        StockEventMessage eventMessage = new StockEventMessage(productId, vendor.getName(), oldStock, newStock);
        eventPublisher.publishStockEvent(eventMessage);
    }
    
    
    @Transactional
    public void syncStock(String productId, int newStock, String vendorName) {
        Vendor vendor = vendorRepository.findByName(vendorName)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found: " + vendorName));

        // Create a transient StockItem that represents the incoming data
        StockItem incoming = StockItem.builder()
                .productId(productId)
                .stock(newStock)
                .vendor(vendor)
                .build();

        // Reuse existing delta processing
        processStockDelta(vendor, incoming);
    }
    
    @Transactional
    public void syncFromVendor(String vendorName) {
        log.info("Triggering vendor-wide sync for: {}", vendorName);

        VendorClient client = vendorClientFactory.getClient(vendorName);
        List<StockItem> stockItems = client.fetchStock(vendorName);

        Vendor vendor = vendorRepository.findByName(vendorName)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found: " + vendorName));

        stockItems.forEach(item -> processStockDelta(vendor, item));
    }
    
    
}
