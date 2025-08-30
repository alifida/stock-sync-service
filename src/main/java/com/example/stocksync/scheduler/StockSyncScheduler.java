package com.example.stocksync.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.stocksync.repository.VendorRepository;
import com.example.stocksync.service.StockSyncService;

import org.springframework.beans.factory.annotation.Value;

@Component
@RequiredArgsConstructor
public class StockSyncScheduler {

    private final VendorRepository vendorRepository;
    private final StockSyncService stockSyncService;

    @Value("${stock.sync.fixed-delay:300000}") 
    private long syncDelay; // default to 5 minutes if not set

    @Scheduled(fixedDelayString = "${stock.sync.fixed-delay}")
    public void scheduledSync() {
        vendorRepository.findAll()
                .forEach(v -> stockSyncService.syncFromVendor(v.getName()));
    }
}

