package com.example.stocksync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StockSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockSyncApplication.class, args);
	}

}
