package com.example.stocksync.web;

import com.example.stocksync.model.StockEvent;
import com.example.stocksync.model.StockItem;
import com.example.stocksync.model.Vendor;
import com.example.stocksync.repository.StockEventRepository;
import com.example.stocksync.repository.StockItemRepository;
import com.example.stocksync.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StockController {

    private final VendorRepository vendorRepository;
    private final StockItemRepository stockItemRepository;
    private final StockEventRepository stockEventRepository;

    // List all vendors
    @GetMapping("/vendors")
    public List<Vendor> getVendors() {
        return vendorRepository.findAll();
    }

    // List stock items, optionally filter by vendor
    @GetMapping("/stocks")
    public List<StockItem> getStocks(@RequestParam(required = false) String vendor) {
        if (vendor != null) {
            return stockItemRepository.findAll()
                    .stream()
                    .filter(item -> item.getVendor().getName().equalsIgnoreCase(vendor))
                    .toList();
        }
        return stockItemRepository.findAll();
    }

    // List events by vendor or productId
    @GetMapping("/events")
    public List<StockEvent> getEvents(
            @RequestParam(required = false) String vendor,
            @RequestParam(required = false) String productId
    ) {
        if (vendor != null) return stockEventRepository.findByVendor_Name(vendor);
        if (productId != null) return stockEventRepository.findByProductId(productId);
        return stockEventRepository.findAll();
    }
}
