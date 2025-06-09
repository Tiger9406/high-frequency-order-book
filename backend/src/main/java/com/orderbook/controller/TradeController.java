package com.orderbook.controller;

import com.orderbook.dto.TradeStats;
import com.orderbook.entity.Trade;
import com.orderbook.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trades")
@CrossOrigin(origins = "*")
public class TradeController {

    @Autowired
    private TradeRepository tradeRepository;

    @GetMapping("/{symbol}")
    public ResponseEntity<List<Trade>> getTradeHistory(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "100") int limit) {
        List<Trade> trades;
        if (limit <= 100) {
            trades = tradeRepository.findTop100BySymbolOrderByExecutedAtDesc(symbol);
        } else {
            trades = tradeRepository.findBySymbolOrderByExecutedAtDesc(symbol);
        }
        return ResponseEntity.ok(trades);
    }

    @GetMapping("/{symbol}/stats")
    public ResponseEntity<TradeStats> getTradeStats(@PathVariable String symbol) {
        LocalDateTime since24h = LocalDateTime.now().minusHours(24);
        List<Trade> trades24h = tradeRepository.findBySymbolAndExecutedAtAfter(symbol, since24h);

        TradeStats stats = new TradeStats(symbol);
        
        if (!trades24h.isEmpty()) {
            // Calculate stats
            BigDecimal volume = trades24h.stream()
                    .map(Trade::getQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal high = trades24h.stream()
                    .map(Trade::getPrice)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            
            BigDecimal low = trades24h.stream()
                    .map(Trade::getPrice)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            Trade latestTrade = trades24h.get(0); // First in desc order
            Trade oldestTrade = trades24h.get(trades24h.size() - 1);
            
            BigDecimal priceChange = latestTrade.getPrice().subtract(oldestTrade.getPrice());

            stats.setLastPrice(latestTrade.getPrice());
            stats.setVolume24h(volume);
            stats.setHigh24h(high);
            stats.setLow24h(low);
            stats.setPriceChange24h(priceChange);
            stats.setTradeCount24h(trades24h.size());
        }

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Trade>> getUserTrades(@PathVariable String userId) {
        List<Trade> trades = tradeRepository.findByUserId(userId);
        return ResponseEntity.ok(trades);
    }
}
