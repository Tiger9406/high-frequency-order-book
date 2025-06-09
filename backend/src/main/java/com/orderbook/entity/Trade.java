package com.orderbook.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String symbol;
    
    @Column(nullable = false)
    private Long buyOrderId;
    
    @Column(nullable = false)
    private Long sellOrderId;
    
    @Column(nullable = false)
    private String buyUserId;
    
    @Column(nullable = false)
    private String sellUserId;
    
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal quantity;
    
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal price;
    
    @Column(nullable = false)
    private LocalDateTime executedAt;

    // Constructors
    public Trade() {}

    public Trade(String symbol, Long buyOrderId, Long sellOrderId, String buyUserId, String sellUserId, BigDecimal quantity, BigDecimal price) {
        this.symbol = symbol;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.buyUserId = buyUserId;
        this.sellUserId = sellUserId;
        this.quantity = quantity;
        this.price = price;
        this.executedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public Long getBuyOrderId() { return buyOrderId; }
    public void setBuyOrderId(Long buyOrderId) { this.buyOrderId = buyOrderId; }

    public Long getSellOrderId() { return sellOrderId; }
    public void setSellOrderId(Long sellOrderId) { this.sellOrderId = sellOrderId; }

    public String getBuyUserId() { return buyUserId; }
    public void setBuyUserId(String buyUserId) { this.buyUserId = buyUserId; }

    public String getSellUserId() { return sellUserId; }
    public void setSellUserId(String sellUserId) { this.sellUserId = sellUserId; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public LocalDateTime getExecutedAt() { return executedAt; }
    public void setExecutedAt(LocalDateTime executedAt) { this.executedAt = executedAt; }
}
