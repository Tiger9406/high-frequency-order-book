package com.orderbook.controller;

import com.orderbook.dto.OrderBookData;
import com.orderbook.service.OrderBookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// controller managing order book data for different trading symbols
@RestController
@RequestMapping("/api/orderbook")
public class OrderBookController {

    @Autowired
    private OrderBookManager orderBookManager;

    @GetMapping("/{symbol}")
    public ResponseEntity<OrderBookData> getOrderBook(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "20") int depth) {
        OrderBookData orderBookData = orderBookManager.getOrderBookData(symbol, depth);
        return ResponseEntity.ok(orderBookData);
    }

    @GetMapping("/symbols")
    public ResponseEntity<List<String>> getActiveSymbols() {
        List<String> symbols = orderBookManager.getActiveSymbols();
        return ResponseEntity.ok(symbols);
    }
}
