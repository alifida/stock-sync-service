package com.example.stocksync.simulator;

import com.example.stocksync.service.StockSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * A utility for running stock sync simulations in Dev/QA environments.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StockSyncSimulator {

    private final StockSyncService stockSyncService;

    /**
     * Runs a simulated vendor sync with hardcoded values.
     */
    public void runSimulation(String vendorName) {
        log.info("Running simulated sync for vendor: {}", vendorName);
        stockSyncService.syncStock("P1001", 8, vendorName);
        stockSyncService.syncStock("P1002", 5, vendorName);
        stockSyncService.syncStock("P1003", 2, vendorName);
    }
}
