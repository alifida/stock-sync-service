package com.example.stocksync;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.stocksync.service.StockSyncService;

@SpringBootTest
class StockSyncServiceTest {

    @Autowired
    private StockSyncService stockSyncService;

    @Test
    void testSyncFromVendor() {
        stockSyncService.syncFromVendor("VendorA");
        // Assertions to validate DB changes
    }
}
