package com.example.stocksync.vendor;

import com.example.stocksync.model.Vendor;
import com.example.stocksync.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VendorClientFactory {

    private final VendorRepository vendorRepository;
    private final ApplicationContext context;

    public VendorClient getClient(String vendorName) {
        Vendor vendor = vendorRepository.findByNameIgnoreCase(vendorName)
                .orElseThrow(() -> new RuntimeException("Vendor not found: " + vendorName));

        String type = vendor.getType().toUpperCase();
        return (VendorClient) context.getBean(type);
    }
}
