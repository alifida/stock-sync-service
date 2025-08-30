package com.example.stocksync.vendor;


import java.util.List;

import com.example.stocksync.model.StockItem;

public interface VendorClient {
    List<StockItem> fetchStock(String vendor);
}
