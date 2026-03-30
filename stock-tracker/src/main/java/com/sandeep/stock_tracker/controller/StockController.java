package com.sandeep.stock_tracker.controller;


import com.sandeep.stock_tracker.dto.DailyStockResponse;
import com.sandeep.stock_tracker.dto.FavoriteStockRequest;
import com.sandeep.stock_tracker.dto.StockOverviewResponse;
import com.sandeep.stock_tracker.dto.StockResponse;
import com.sandeep.stock_tracker.entity.FavoriteStock;
import com.sandeep.stock_tracker.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {

    private final StockService stockService;
    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }
    @GetMapping("/{stockSymbol}")
    public Mono<StockResponse> getStock(@PathVariable String stockSymbol){
        return  stockService.getStockForSymbol(stockSymbol.toUpperCase());
    }

    @GetMapping("/{stockSymbol}/overview")
    public Mono<StockOverviewResponse> getStockOverview(@PathVariable String stockSymbol){
        return stockService.getStockOverviewForSymbol(stockSymbol.toUpperCase());
    }
    @GetMapping("/{stockSymbol}/history")
    public Mono<List<DailyStockResponse>> getStockHistory(@PathVariable String stockSymbol, @RequestParam(defaultValue = "30") int days){
        return stockService.getHistory(stockSymbol.toUpperCase(),days);
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteStock> saveFavoriteStock(@RequestBody FavoriteStockRequest request){
        final FavoriteStock saved= stockService.addFavorite(request.getSymbol());
        return ResponseEntity.ok(saved);
    }
    @GetMapping("/favorites")
    public Mono<List<StockResponse>> getFavoritesWithPrices(){
        return stockService.getFavortiesWithLivePrices();

    }
}
