package com.orderbook.dto;

import java.math.BigDecimal;

public class OrderRequest {
    private String symbol;
    private String side; // BUY or SELL
    private String type; // MARKET or LIMIT
    private BigDecimal quantity;
    private BigDecimal price;
    private String userId;

    // Constructors
    public OrderRequest() {}

    public OrderRequest(String symbol, String side, String type, BigDecimal quantity, BigDecimal price, String userId) {
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.userId = userId;
    }

    // Getters and Setters
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getSide() { return side; }
    public void setSide(String side) { this.side = side; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
