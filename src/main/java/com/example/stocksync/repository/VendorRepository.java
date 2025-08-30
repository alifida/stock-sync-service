package com.example.stocksync.repository;


import com.example.stocksync.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByName(String name);           // <-- matches vendorRepository.findByName(vendorName)
    Optional<Vendor> findByNameIgnoreCase(String name); // handy variant if you want case-insensitive
}