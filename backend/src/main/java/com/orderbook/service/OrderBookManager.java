package com.orderbook.service;

import com.orderbook.dto.OrderBookData;
import com.orderbook.entity.Order;
import com.orderbook.model.OrderBook;
import com.orderbook.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderBookManager {

    //manages all order books for different symbols
    private final ConcurrentHashMap<String, OrderBook> orderBooks = new ConcurrentHashMap<>();

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebSocketService webSocketService;

    public OrderBook getOrderBook(String symbol) {
        return orderBooks.computeIfAbsent(symbol, this::createOrderBook);
    }

    //creates a new order book for the given symbol; //also loads existing active orders from the database
    //if already exists, it returns the existing one
    private OrderBook createOrderBook(String symbol) {
        OrderBook orderBook = new OrderBook(symbol);
        
        // Load existing active orders from database
        List<Order> activeOrders = orderRepository.findActiveOrdersBySymbol(symbol);
        for (Order order : activeOrders) {
            orderBook.addOrder(order);
        }
        
        return orderBook;
    }

    public void addOrderToBook(Order order) {
        OrderBook orderBook = getOrderBook(order.getSymbol());
        orderBook.addOrder(order);
        broadcastOrderBookUpdate(order.getSymbol());
    }

    public void removeOrderFromBook(String symbol, Long orderId) {
        OrderBook orderBook = orderBooks.get(symbol);
        if (orderBook != null) {
            orderBook.removeOrder(orderId);
            broadcastOrderBookUpdate(symbol);
        }
    }

    public OrderBookData getOrderBookData(String symbol, int depth) {
        OrderBook orderBook = getOrderBook(symbol);
        return new OrderBookData(
            symbol,
            orderBook.getBids(depth),
            orderBook.getAsks(depth)
        );
    }

    //broadcast to all connected clients
    public void broadcastOrderBookUpdate(String symbol) {
        OrderBookData data = getOrderBookData(symbol, 20); // Top 20 levels
        webSocketService.broadcastOrderBook(data);
    }

    public List<String> getActiveSymbols() {
        return orderBooks.keySet().stream().toList();
    }
}
