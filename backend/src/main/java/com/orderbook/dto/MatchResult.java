package com.orderbook.dto;

import com.orderbook.entity.Trade;
import java.util.List;

public class MatchResult {
    private List<Trade> trades;
    private boolean fullyMatched;

    // Constructors
    public MatchResult() {}

    public MatchResult(List<Trade> trades, boolean fullyMatched) {
        this.trades = trades;
        this.fullyMatched = fullyMatched;
    }

    // Getters and Setters
    public List<Trade> getTrades() { return trades; }
    public void setTrades(List<Trade> trades) { this.trades = trades; }

    public boolean isFullyMatched() { return fullyMatched; }
    public void setFullyMatched(boolean fullyMatched) { this.fullyMatched = fullyMatched; }
}
