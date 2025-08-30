package com.example.stocksync.vendor;


import com.example.stocksync.model.StockItem;
import java.util.List;

public interface VendorClient {
    List<StockItem> fetchStock(String vendorName);
}
