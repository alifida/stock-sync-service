package com.example.stocksync.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stocksync.model.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByName(String name);           
    Optional<Vendor> findByNameIgnoreCase(String name); 
    List<Vendor> findByEnabledTrue();
}