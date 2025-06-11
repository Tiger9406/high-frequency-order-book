package com.orderbook.dto;

import com.orderbook.entity.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// to represent an order response to be sent back to the user
public class OrderResponse {
    private Long id;
    private String symbol;
    private String side;
    private String type;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal remainingQuantity;
    private String status;
    private String userId;
    private LocalDateTime createdAt;

    // Constructors
    public OrderResponse() {}

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.symbol = order.getSymbol();
        this.side = order.getSide().name();
        this.type = order.getType().name();
        this.quantity = order.getQuantity();
        this.price = order.getPrice();
        this.remainingQuantity = order.getRemainingQuantity();
        this.status = order.getStatus().name();
        this.userId = order.getUserId();
        this.createdAt = order.getCreatedAt();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public BigDecimal getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(BigDecimal remainingQuantity) { this.remainingQuantity = remainingQuantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
