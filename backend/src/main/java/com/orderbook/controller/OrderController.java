package com.orderbook.controller;

import com.orderbook.dto.MatchResult;
import com.orderbook.dto.OrderRequest;
import com.orderbook.dto.OrderResponse;
import com.orderbook.entity.Order;
import com.orderbook.model.OrderBook;
import com.orderbook.repository.OrderRepository;
import com.orderbook.service.MatchingEngine;
import com.orderbook.service.OrderBookManager;
import com.orderbook.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderBookManager orderBookManager;

    @Autowired
    private MatchingEngine matchingEngine;

    @Autowired
    private WebSocketService webSocketService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request) {
        try {
            // Create order entity
            Order order = new Order(
                    request.getSymbol(),
                    Order.OrderSide.valueOf(request.getSide().toUpperCase()),
                    Order.OrderType.valueOf(request.getType().toUpperCase()),
                    request.getQuantity(),
                    request.getPrice(),
                    request.getUserId());

            // Save order to database
            order = orderRepository.save(order);

            // Get order book for the symbol
            OrderBook orderBook = orderBookManager.getOrderBook(request.getSymbol());

            // Attempt to match the order
            MatchResult matchResult = matchingEngine.matchOrder(order, orderBook);

            // Add remaining quantity to order book if not fully matched
            if (!matchResult.isFullyMatched() && order.getType() == Order.OrderType.LIMIT) {
                orderBookManager.addOrderToBook(order); // This already broadcasts
            } else {
                // If order was fully matched or was a market order, broadcast update
                orderBookManager.broadcastOrderBookUpdate(order.getSymbol());
            }

            // Send order update to user
            webSocketService.sendOrderUpdate(order.getUserId(), new OrderResponse(order));

            return ResponseEntity.ok(new OrderResponse(order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable String userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<OrderResponse> responses = orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId, @RequestParam String userId) {
        try {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order == null || !order.getUserId().equals(userId)) {
                return ResponseEntity.notFound().build();
            }

            if (order.getStatus() == Order.OrderStatus.FILLED) {
                return ResponseEntity.badRequest().build();
            }

            // Update order status
            order.setStatus(Order.OrderStatus.CANCELLED);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);

            // Remove from order book
            orderBookManager.removeOrderFromBook(order.getSymbol(), orderId);

            // Send update to user
            webSocketService.sendOrderUpdate(userId, new OrderResponse(order));

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new OrderResponse(order));
    }
}
