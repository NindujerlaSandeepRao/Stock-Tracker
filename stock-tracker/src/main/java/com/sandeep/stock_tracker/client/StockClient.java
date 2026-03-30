package com.sandeep.stock_tracker.client;


import com.sandeep.stock_tracker.dto.AlphaVantageResponse;
import com.sandeep.stock_tracker.dto.DailyStockResponse;
import com.sandeep.stock_tracker.dto.StockHistoryResponse;
import com.sandeep.stock_tracker.dto.StockOverviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockClient {
    private final WebClient webClient;

    @Value("${alpha.vantage.api.key}")
    private String apiKey;

    public Mono<AlphaVantageResponse> getStockQuote(String symbol){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function","GLOBAL_QUOTE")
                        .queryParam("symbol",symbol)
                        .queryParam("apikey",apiKey)
                        .build())
                .retrieve()
                .bodyToMono(AlphaVantageResponse.class);
    }

    public Mono<StockOverviewResponse> getStockOverview(String symbol) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function","OVERVIEW")
                        .queryParam("symbol",symbol)
                        .queryParam("apikey",apiKey)
                        .build())
                .retrieve()
                .bodyToMono(StockOverviewResponse.class);
    }

    public Mono<StockHistoryResponse> getHistory(String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function","TIME_SERIES_DAILY")
                        .queryParam("symbol",symbol)
                        .queryParam("apikey",apiKey)
                        .build())
                .retrieve()
                .bodyToMono(StockHistoryResponse.class);
    }
}
