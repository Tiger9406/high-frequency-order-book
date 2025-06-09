package com.orderbook.dto;

import java.math.BigDecimal;

public class TradeStats {
    private String symbol;
    private BigDecimal lastPrice;
    private BigDecimal volume24h;
    private BigDecimal priceChange24h;
    private BigDecimal high24h;
    private BigDecimal low24h;
    private long tradeCount24h;

    // Constructors
    public TradeStats() {}

    public TradeStats(String symbol) {
        this.symbol = symbol;
        this.volume24h = BigDecimal.ZERO;
        this.priceChange24h = BigDecimal.ZERO;
        this.tradeCount24h = 0;
    }

    // Getters and Setters
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public BigDecimal getLastPrice() { return lastPrice; }
    public void setLastPrice(BigDecimal lastPrice) { this.lastPrice = lastPrice; }

    public BigDecimal getVolume24h() { return volume24h; }
    public void setVolume24h(BigDecimal volume24h) { this.volume24h = volume24h; }

    public BigDecimal getPriceChange24h() { return priceChange24h; }
    public void setPriceChange24h(BigDecimal priceChange24h) { this.priceChange24h = priceChange24h; }

    public BigDecimal getHigh24h() { return high24h; }
    public void setHigh24h(BigDecimal high24h) { this.high24h = high24h; }

    public BigDecimal getLow24h() { return low24h; }
    public void setLow24h(BigDecimal low24h) { this.low24h = low24h; }

    public long getTradeCount24h() { return tradeCount24h; }
    public void setTradeCount24h(long tradeCount24h) { this.tradeCount24h = tradeCount24h; }
}
