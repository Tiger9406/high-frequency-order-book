package com.orderbook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderbook.dto.OrderBookData;
import com.orderbook.entity.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void broadcastOrderBook(OrderBookData orderBookData) {
        try {
            messagingTemplate.convertAndSend("/topic/orderbook/" + orderBookData.getSymbol(), orderBookData);
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Failed to broadcast order book update: " + e.getMessage());
        }
    }

    public void broadcastTrade(Trade trade) {
        try {
            messagingTemplate.convertAndSend("/topic/trades/" + trade.getSymbol(), trade);
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Failed to broadcast trade: " + e.getMessage());
        }
    }

    public void sendOrderUpdate(String userId, Object orderUpdate) {
        try {
            messagingTemplate.convertAndSend("/topic/orders/" + userId, orderUpdate);
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Failed to send order update: " + e.getMessage());
        }
    }
}
