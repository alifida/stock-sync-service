package com.example.stocksync.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stocksync.service.StockSyncService;

@RestController
@RequestMapping("/sync")
public class StockSyncController {

    private final StockSyncService stockSyncService;

    public StockSyncController(StockSyncService stockSyncService) {
        this.stockSyncService = stockSyncService;
    }

    @PostMapping("/{vendor}")
    public String syncVendorStock(@PathVariable String vendorName) {
        stockSyncService.syncFromVendor(vendorName);
        return "Stock sync triggered for vendor: " + vendorName;
    }
}
