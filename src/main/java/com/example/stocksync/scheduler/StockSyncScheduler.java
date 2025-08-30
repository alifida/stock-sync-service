package com.example.stocksync.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.stocksync.repository.VendorRepository;
import com.example.stocksync.service.StockSyncService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j 

public class StockSyncScheduler {

    private final VendorRepository vendorRepository;
    private final StockSyncService stockSyncService;

    @Value("${stock.sync.fixed-delay:300000}") 
    private long syncDelay; // default to 5 minutes if not set

    @Scheduled(fixedRate = 60000)
    public void scheduledSync() {
        try {
            vendorRepository.findAll()
                    .forEach(v -> stockSyncService.syncFromVendor(v.getName()));
        } catch (Exception e) {
            log.error("Scheduled sync failed: {}", e.getMessage(), e);
        }
    }
}

