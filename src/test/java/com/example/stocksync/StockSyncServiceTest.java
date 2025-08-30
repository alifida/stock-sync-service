package com.example.stocksync;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.stocksync.model.Vendor;
import com.example.stocksync.repository.VendorRepository;
import com.example.stocksync.service.StockSyncService;

@SpringBootTest
class StockSyncServiceTest {

    @Autowired
    private StockSyncService stockSyncService;

    @Autowired
    private VendorRepository vendorRepository;
    
    @Test
    void testSyncFromVendor() {
        stockSyncService.syncFromVendor("VendorA");
        // Assertions to validate DB changes
    }

    @Test
    public void testSyncOnlyEnabledVendors() {
        Vendor enabledVendor = new Vendor("A", true);
        enabledVendor.setType("CSV");
        Vendor disabledVendor = new Vendor("B", false);
        disabledVendor.setType("CSV");

        vendorRepository.save(enabledVendor);
        vendorRepository.save(disabledVendor);

        stockSyncService.syncStock();

        // Assert only enabledVendor's stock is updated
    }
}
