package com.orderbook.dto;

import java.math.BigDecimal;

public class PriceLevel {
    private BigDecimal price;
    private BigDecimal quantity;
    private int orderCount;

    // Constructors
    public PriceLevel() {}

    public PriceLevel(BigDecimal price, BigDecimal quantity, int orderCount) {
        this.price = price;
        this.quantity = quantity;
        this.orderCount = orderCount;
    }

    // Getters and Setters
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public int getOrderCount() { return orderCount; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }
}
