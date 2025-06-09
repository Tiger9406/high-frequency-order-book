package com.orderbook.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String symbol;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderSide side;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType type;
    
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal quantity;
    
    @Column(precision = 19, scale = 8)
    private BigDecimal price;
    
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal remainingQuantity;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    @Column(nullable = false)
    private String userId;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime updatedAt;

    public enum OrderSide {
        BUY, SELL
    }

    public enum OrderType {
        MARKET, LIMIT
    }

    public enum OrderStatus {
        PENDING, PARTIAL_FILLED, FILLED, CANCELLED
    }

    // Constructors
    public Order() {}

    public Order(String symbol, OrderSide side, OrderType type, BigDecimal quantity, BigDecimal price, String userId) {
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.remainingQuantity = quantity;
        this.status = OrderStatus.PENDING;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public OrderSide getSide() { return side; }
    public void setSide(OrderSide side) { this.side = side; }

    public OrderType getType() { return type; }
    public void setType(OrderType type) { this.type = type; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(BigDecimal remainingQuantity) { this.remainingQuantity = remainingQuantity; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
