package com.example.stocksync.service;


import com.example.stocksync.model.Vendor;
import com.example.stocksync.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    public Vendor getVendorByName(String vendorName) {
        return vendorRepository.findByNameIgnoreCase(vendorName)
                .orElseThrow(() -> new RuntimeException("Vendor not found: " + vendorName));
    }
}
