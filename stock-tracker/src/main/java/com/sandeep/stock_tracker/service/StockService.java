package com.sandeep.stock_tracker.service;

import com.sandeep.stock_tracker.client.StockClient;
import com.sandeep.stock_tracker.dto.*;
import com.sandeep.stock_tracker.entity.FavoriteStock;
import com.sandeep.stock_tracker.exception.FavoriteAlreadyExistsException;
import com.sandeep.stock_tracker.repository.FavoriteStockRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {

    private StockClient stockClient;
    private FavoriteStockRepository favoriteStockRepository;

    public StockService(final StockClient stockClient,final FavoriteStockRepository favoriteStockRepository) {
        this.stockClient = stockClient;
        this.favoriteStockRepository=favoriteStockRepository;
    }
//    @Cacheable(value = "stocks",key = "#stockSymbol")
    public Mono<StockResponse> getStockForSymbol(final String stockSymbol ) {
//       AlphaVantageResponse response = stockClient.getStockQuote(stockSymbol);
//
//        return  StockResponse.builder()
//                .symbol(response.globalQuote().symbol())
//                .price(Double.parseDouble(response.globalQuote().price()))
//                .lastUpdated(response.globalQuote().lastTradingDay())
//                .build();

//        return stockClient.getStockQuote(stockSymbol)
//                .map(response -> StockResponse.builder()
//                        .symbol(response.globalQuote().symbol())
//                        .price(Double.parseDouble(response.globalQuote().price()))
//                        .lastUpdated(response.globalQuote().lastTradingDay())
//                        .build());
        return stockClient.getStockQuote(stockSymbol)
                .flatMap(response -> {

                    if (response.globalQuote() == null) {
                        return Mono.error(
                                new RuntimeException("No data for symbol: " + stockSymbol)
                        );
                    }

                    return Mono.just(
                            StockResponse.builder()
                                    .symbol(response.globalQuote().symbol())
                                    .price(Double.parseDouble(response.globalQuote().price()))
                                    .lastUpdated(response.globalQuote().lastTradingDay())
                                    .build()
                    );
                });
    }

    public Mono<StockOverviewResponse> getStockOverviewForSymbol(final String symbol) {

        return stockClient.getStockOverview(symbol);
    }

    public Mono<List<DailyStockResponse>> getHistory(final String symbol, int days) {
        return stockClient.getHistory(symbol)
                .map(response ->
                        response.timeSeries().entrySet().stream()
                                .limit(days)
                                .map(entry -> {
                                    var date = entry.getKey();
                                    var daily = entry.getValue();
                                    return new DailyStockResponse(
                                            date,
                                            Double.parseDouble(daily.open()),
                                            Double.parseDouble(daily.close()),
                                            Double.parseDouble(daily.high()),
                                            Double.parseDouble(daily.low()),
                                            Long.parseLong(daily.volume())
                                    );
                                })
                                .collect(Collectors.toList())
                );
    }
    @Transactional
    public FavoriteStock addFavorite(final String symbol){
        if(favoriteStockRepository.existsBySymbol(symbol)){
            throw new FavoriteAlreadyExistsException(symbol);
        }
        FavoriteStock favoriteStock=FavoriteStock.builder()
                .symbol(symbol)
                .build();
        return favoriteStockRepository.save(favoriteStock);

    }

    public Mono<List<StockResponse>> getFavortiesWithLivePrices() {

        List<FavoriteStock> favoriteStocks = favoriteStockRepository.findAll();

        return reactor.core.publisher.Flux.fromIterable(favoriteStocks)
                .flatMap(fav ->
                        getStockForSymbol(fav.getSymbol())
                                .onErrorResume(ex -> {
                                    // fallback if API fails
                                    return Mono.just(
                                            StockResponse.builder()
                                                    .symbol(fav.getSymbol())
                                                    .price(0.0)
                                                    .lastUpdated("N/A")
                                                    .build()
                                    );
                                })
                )
                .collectList();
    }
}
