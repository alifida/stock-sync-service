package com.example.stocksync.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.stocksync.model.StockEvent;
import com.example.stocksync.model.StockItem;
import com.example.stocksync.model.Vendor;
import com.example.stocksync.repository.StockEventRepository;
import com.example.stocksync.repository.StockItemRepository;
import com.example.stocksync.repository.VendorRepository;
import com.example.stocksync.service.StockSyncService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class StockController {

    private final VendorRepository vendorRepository;
    private final StockItemRepository stockItemRepository;
    private final StockEventRepository stockEventRepository;

    @Autowired
    private StockSyncService stockSyncService;

    // List all vendors
    @Operation(summary = "List all vendors")
    @GetMapping("/vendors")
    public List<Vendor> getVendors() {
        return vendorRepository.findAll();
    }

    // List stock items, optionally filter by vendor
    @Operation(summary = "List all products with current stock")
    @GetMapping
    public List<StockItem> getAllProducts(@RequestParam(required = false) String vendor) {
        if (vendor != null) {
            return stockItemRepository.findAll()
                    .stream()
                    .filter(item -> item.getVendor().equalsIgnoreCase(vendor))
                    .toList();
        }
        return stockSyncService.getAllCurrentStock();
    }

    // List events by vendor or sku
    @Operation(summary = "List stock events by vendor or SKU")
    @GetMapping("/events")
    public List<StockEvent> getEvents(
            @RequestParam(required = false) String vendor,
            @RequestParam(required = false) String sku
    ) {
        if (vendor != null) return stockEventRepository.findByVendor(vendor);
        if (sku != null) return stockEventRepository.findBySku(sku);
        return stockEventRepository.findAll();
    }
}
