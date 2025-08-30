package com.example.stocksync.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.stocksync.event.StockEventMessage;
import com.example.stocksync.event.StockEventPublisher;
import com.example.stocksync.model.StockEvent;
import com.example.stocksync.model.StockItem;
import com.example.stocksync.model.Vendor;
import com.example.stocksync.repository.StockEventRepository;
import com.example.stocksync.repository.StockItemRepository;
import com.example.stocksync.repository.VendorRepository;
import com.example.stocksync.vendor.VendorClient;
import com.example.stocksync.vendor.VendorClientFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
                .findBySkuAndVendor(item.getSku(), vendor.getName())
                .orElse(StockItem.builder()
                        .id(item.getId())
                        .vendor(vendor.getName())
                        .sku(item.getSku())
                        .name(item.getName())
                        .stockQuantity(0)
                        .build());

        int oldStock = stockItem.getStockQuantity();
        int newStock = item.getStockQuantity();

        if (oldStock != newStock) {
            stockItem.setStockQuantity(newStock);
            System.out.println("====================");
            System.out.println(stockItem);
            stockItemRepository.save(stockItem);
            saveAndPublishEvent(item.getSku(), vendor.getName(), oldStock, newStock);
            log.info("Stock updated for product {}: {} -> {}", item.getId(), oldStock, newStock);
        } else {
            log.debug("No stock change for product {}", item.getId());
        }
    }

    /**
     * Saves a StockEvent in the database and publishes it to the message queue.
     */
    private void saveAndPublishEvent(String sku, String vendor, int oldStock, int newStock) {
        if (oldStock > 0 && newStock == 0) {
            log.info("Product {} from vendor {} went out of stock.", sku, vendor);
            StockEvent stockEvent = StockEvent.builder()
                    .sku(sku)
                    .vendor(vendor)
                    .oldStock(oldStock)
                    .newStock(newStock)
                    .eventType("OUT_OF_STOCK")
                    .build();
            stockEventRepository.save(stockEvent);

            StockEventMessage eventMessage = new StockEventMessage(sku, vendor, oldStock, newStock);
            eventPublisher.publishStockEvent(eventMessage);
        }
    }
    
    
    @Transactional
    public void syncStock(String sku, int newStock, String vendorName) {
        Vendor vendor = vendorRepository.findByName(vendorName)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found: " + vendorName));

        // Create a transient StockItem that represents the incoming data
        StockItem incoming = StockItem.builder()
                .sku(sku)
                .stockQuantity(newStock)
                .vendor(vendor.getName())
                .build();

        // Reuse existing delta processing
        processStockDelta(vendor, incoming);
    }
    
 
    
    @Transactional
    public void syncFromVendor(String vendorName) {
        log.info("Triggering vendor-wide sync for: {}", vendorName);

        Vendor vendor = vendorRepository.findByName(vendorName)
                .orElseGet(() -> {
                    log.info("Vendor not found. Creating new vendor: {}", vendorName);
                    Vendor newVendor = new Vendor();
                    newVendor.setName(vendorName);
                    newVendor.setType("CSV");
                    newVendor.setEnabled(true);
                    return vendorRepository.save(newVendor);
                });

        VendorClient client = vendorClientFactory.getClient(vendorName);
        List<StockItem> stockItems = client.fetchStock(vendorName);

        stockItems.forEach(item -> processStockDelta(vendor, item));
    }
    
    @Transactional
    public void syncStock() {
        List<Vendor> enabledVendors = vendorRepository.findByEnabledTrue();
        for (Vendor vendor : enabledVendors) {
            VendorClient client = vendorClientFactory.getClient(vendor.getName());
            List<StockItem> items = client.fetchStock(vendor.getName());
            for (StockItem item : items) {
                processStockDelta(vendor, item);
            }
        }
    }



    

    public List<StockItem> getAllCurrentStock() {
        return stockItemRepository.findAll();
    }
}
