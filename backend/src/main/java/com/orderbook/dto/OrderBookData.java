package com.orderbook.dto;

import java.util.List;

public class OrderBookData {
    private String symbol;
    private List<PriceLevel> bids;
    private List<PriceLevel> asks;
    private long timestamp;

    // Constructors
    public OrderBookData() {}

    public OrderBookData(String symbol, List<PriceLevel> bids, List<PriceLevel> asks) {
        this.symbol = symbol;
        this.bids = bids;
        this.asks = asks;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public List<PriceLevel> getBids() { return bids; }
    public void setBids(List<PriceLevel> bids) { this.bids = bids; }

    public List<PriceLevel> getAsks() { return asks; }
    public void setAsks(List<PriceLevel> asks) { this.asks = asks; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
