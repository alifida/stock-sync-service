package com.example.stocksync.vendor.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.stocksync.model.StockItem;
import com.example.stocksync.vendor.VendorClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("CSV")
public class CsvVendorClient implements VendorClient {
    @Override
    public List<StockItem> fetchStock(String vendorName) {
        log.info("Fetching stock via CSV for vendor: {}", vendorName);
        List<StockItem> items = new java.util.ArrayList<>();
        String csvPath = "/tmp/vendor-b/stock.csv";
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(csvPath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // skip header
                String[] parts = line.split(",");
                if (parts.length < 3) continue;
                String sku = parts[0].trim();
                String name = parts[1].trim();
                int stockQuantity = Integer.parseInt(parts[2].trim());
                StockItem item = StockItem.builder()
                    .sku(sku)
                    .name(name)
                    .stockQuantity(stockQuantity)
                    .vendor(vendorName)
                    .build();
                System.out.println("-----------------------");
                
                System.out.println(item.toString());
                System.out.println("-----------------------");
                
                items.add(item);
            }
        } catch (Exception e) {
            log.error("CSV fetch failed for vendor {}: {}", vendorName, e.getMessage(), e);
            return List.of(); // Return empty list or handle as needed
        }
        
        
        
        return items;
    }
}
