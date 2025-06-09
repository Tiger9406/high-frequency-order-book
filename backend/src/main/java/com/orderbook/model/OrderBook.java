package com.orderbook.model;

import com.orderbook.dto.PriceLevel;
import com.orderbook.entity.Order;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class OrderBook {
    private final String symbol;
    private final ConcurrentSkipListMap<BigDecimal, List<Order>> bids; // Price descending
    private final ConcurrentSkipListMap<BigDecimal, List<Order>> asks; // Price ascending
    private final Map<Long, Order> orderMap;

    public OrderBook(String symbol) {
        this.symbol = symbol;
        this.bids = new ConcurrentSkipListMap<>(Collections.reverseOrder());
        this.asks = new ConcurrentSkipListMap<>();
        this.orderMap = new ConcurrentHashMap<>();
    }

    public void addOrder(Order order) {
        orderMap.put(order.getId(), order);
        
        if (order.getSide() == Order.OrderSide.BUY) {
            bids.computeIfAbsent(order.getPrice(), k -> new ArrayList<>()).add(order);
        } else {
            asks.computeIfAbsent(order.getPrice(), k -> new ArrayList<>()).add(order);
        }
    }

    public void removeOrder(Long orderId) {
        Order order = orderMap.remove(orderId);
        if (order != null) {
            if (order.getSide() == Order.OrderSide.BUY) {
                List<Order> ordersAtPrice = bids.get(order.getPrice());
                if (ordersAtPrice != null) {
                    ordersAtPrice.remove(order);
                    if (ordersAtPrice.isEmpty()) {
                        bids.remove(order.getPrice());
                    }
                }
            } else {
                List<Order> ordersAtPrice = asks.get(order.getPrice());
                if (ordersAtPrice != null) {
                    ordersAtPrice.remove(order);
                    if (ordersAtPrice.isEmpty()) {
                        asks.remove(order.getPrice());
                    }
                }
            }
        }
    }

    public List<PriceLevel> getBids(int depth) {
        return getPriceLevels(bids, depth);
    }

    public List<PriceLevel> getAsks(int depth) {
        return getPriceLevels(asks, depth);
    }

    private List<PriceLevel> getPriceLevels(ConcurrentSkipListMap<BigDecimal, List<Order>> side, int depth) {
        List<PriceLevel> levels = new ArrayList<>();
        int count = 0;
        
        for (Map.Entry<BigDecimal, List<Order>> entry : side.entrySet()) {
            if (count >= depth) break;
            
            BigDecimal totalQuantity = entry.getValue().stream()
                    .map(Order::getRemainingQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            if (totalQuantity.compareTo(BigDecimal.ZERO) > 0) {
                levels.add(new PriceLevel(entry.getKey(), totalQuantity, entry.getValue().size()));
                count++;
            }
        }
        
        return levels;
    }

    public BigDecimal getBestBid() {
        return bids.isEmpty() ? null : bids.firstKey();
    }

    public BigDecimal getBestAsk() {
        return asks.isEmpty() ? null : asks.firstKey();
    }

    public Order getBestBidOrder() {
        if (bids.isEmpty()) return null;
        List<Order> orders = bids.firstEntry().getValue();
        return orders.isEmpty() ? null : orders.get(0);
    }

    public Order getBestAskOrder() {
        if (asks.isEmpty()) return null;
        List<Order> orders = asks.firstEntry().getValue();
        return orders.isEmpty() ? null : orders.get(0);
    }

    public String getSymbol() {
        return symbol;
    }

    public Map<Long, Order> getOrderMap() {
        return orderMap;
    }
}
