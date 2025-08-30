package com.example.stocksync.vendor.impl;

import com.example.stocksync.model.StockItem;
import com.example.stocksync.vendor.VendorClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("API")
public class ApiVendorClient implements VendorClient {
    @Override
    public List<StockItem> fetchStock(String vendorName) {
        log.info("Fetching stock via API for vendor: {}", vendorName);
        // TODO: Implement API call logic
        return List.of();
    }
}
