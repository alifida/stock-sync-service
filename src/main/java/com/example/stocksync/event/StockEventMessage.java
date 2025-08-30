package com.example.stocksync.event;

import lombok.Data;
import java.io.Serializable;
import java.time.Instant;

@Data
public class StockEventMessage implements Serializable {
    private static final long serialVersionUID = -345282279499949306L;
	private String productId;
    private String vendor;
    private int oldStock;
    private int newStock;
    private Instant timestamp;

    public StockEventMessage() {}

    public StockEventMessage(String productId, String vendor, int oldStock, int newStock) {
        this.productId = productId;
        this.vendor = vendor;
        this.oldStock = oldStock;
        this.newStock = newStock;
        this.timestamp = Instant.now();
    }


}
