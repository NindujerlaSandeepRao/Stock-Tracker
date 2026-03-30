package com.sandeep.stock_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class StockTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockTrackerApplication.class, args);
	}

}
