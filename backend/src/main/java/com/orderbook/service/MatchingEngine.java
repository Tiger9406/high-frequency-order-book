package com.orderbook.service;

import com.orderbook.dto.MatchResult;
import com.orderbook.entity.Order;
import com.orderbook.entity.Trade;
import com.orderbook.model.OrderBook;
import com.orderbook.repository.OrderRepository;
import com.orderbook.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchingEngine {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private WebSocketService webSocketService;

    @Transactional
    public MatchResult matchOrder(Order incomingOrder, OrderBook orderBook) {
        List<Trade> trades = new ArrayList<>();
        boolean fullyMatched = false;

        if (incomingOrder.getType() == Order.OrderType.MARKET) {
            fullyMatched = matchMarketOrder(incomingOrder, orderBook, trades);
        } else {
            fullyMatched = matchLimitOrder(incomingOrder, orderBook, trades);
        }

        // Save trades to database
        for (Trade trade : trades) {
            tradeRepository.save(trade);
            webSocketService.broadcastTrade(trade);
        }

        // Update order status
        updateOrderStatus(incomingOrder);
        orderRepository.save(incomingOrder);

        return new MatchResult(trades, fullyMatched);
    }

    private boolean matchMarketOrder(Order marketOrder, OrderBook orderBook, List<Trade> trades) {
        if (marketOrder.getSide() == Order.OrderSide.BUY) {
            return matchAgainstAsks(marketOrder, orderBook, trades);
        } else {
            return matchAgainstBids(marketOrder, orderBook, trades);
        }
    }

    private boolean matchLimitOrder(Order limitOrder, OrderBook orderBook, List<Trade> trades) {
        if (limitOrder.getSide() == Order.OrderSide.BUY) {
            return matchBuyLimitOrder(limitOrder, orderBook, trades);
        } else {
            return matchSellLimitOrder(limitOrder, orderBook, trades);
        }
    }

    private boolean matchBuyLimitOrder(Order buyOrder, OrderBook orderBook, List<Trade> trades) {
        while (buyOrder.getRemainingQuantity().compareTo(BigDecimal.ZERO) > 0) {
            Order bestAsk = orderBook.getBestAskOrder();
            if (bestAsk == null || bestAsk.getPrice().compareTo(buyOrder.getPrice()) > 0) {
                break; // No more matching opportunities
            }

            BigDecimal tradeQuantity = buyOrder.getRemainingQuantity().min(bestAsk.getRemainingQuantity());
            executeTrade(buyOrder, bestAsk, tradeQuantity, bestAsk.getPrice(), trades, orderBook);
        }

        return buyOrder.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0;
    }

    private boolean matchSellLimitOrder(Order sellOrder, OrderBook orderBook, List<Trade> trades) {
        while (sellOrder.getRemainingQuantity().compareTo(BigDecimal.ZERO) > 0) {
            Order bestBid = orderBook.getBestBidOrder();
            if (bestBid == null || bestBid.getPrice().compareTo(sellOrder.getPrice()) < 0) {
                break; // No more matching opportunities
            }

            BigDecimal tradeQuantity = sellOrder.getRemainingQuantity().min(bestBid.getRemainingQuantity());
            executeTrade(bestBid, sellOrder, tradeQuantity, bestBid.getPrice(), trades, orderBook);
        }

        return sellOrder.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0;
    }

    private boolean matchAgainstAsks(Order buyOrder, OrderBook orderBook, List<Trade> trades) {
        while (buyOrder.getRemainingQuantity().compareTo(BigDecimal.ZERO) > 0) {
            Order bestAsk = orderBook.getBestAskOrder();
            if (bestAsk == null) {
                break; // No more liquidity
            }

            BigDecimal tradeQuantity = buyOrder.getRemainingQuantity().min(bestAsk.getRemainingQuantity());
            executeTrade(buyOrder, bestAsk, tradeQuantity, bestAsk.getPrice(), trades, orderBook);
        }

        return buyOrder.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0;
    }

    private boolean matchAgainstBids(Order sellOrder, OrderBook orderBook, List<Trade> trades) {
        while (sellOrder.getRemainingQuantity().compareTo(BigDecimal.ZERO) > 0) {
            Order bestBid = orderBook.getBestBidOrder();
            if (bestBid == null) {
                break; // No more liquidity
            }

            BigDecimal tradeQuantity = sellOrder.getRemainingQuantity().min(bestBid.getRemainingQuantity());
            executeTrade(bestBid, sellOrder, tradeQuantity, bestBid.getPrice(), trades, orderBook);
        }

        return sellOrder.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0;
    }

    private void executeTrade(Order buyOrder, Order sellOrder, BigDecimal quantity, BigDecimal price, 
                             List<Trade> trades, OrderBook orderBook) {
        // Create trade
        Trade trade = new Trade(
            buyOrder.getSymbol(),
            buyOrder.getId(),
            sellOrder.getId(),
            buyOrder.getUserId(),
            sellOrder.getUserId(),
            quantity,
            price
        );
        trades.add(trade);

        // Update order quantities
        buyOrder.setRemainingQuantity(buyOrder.getRemainingQuantity().subtract(quantity));
        sellOrder.setRemainingQuantity(sellOrder.getRemainingQuantity().subtract(quantity));

        // Update order statuses and timestamps
        updateOrderStatus(buyOrder);
        updateOrderStatus(sellOrder);
        
        buyOrder.setUpdatedAt(LocalDateTime.now());
        sellOrder.setUpdatedAt(LocalDateTime.now());

        // Save updated orders
        orderRepository.save(buyOrder);
        orderRepository.save(sellOrder);

        // Send order updates to both users involved in the trade
        webSocketService.sendOrderUpdate(buyOrder.getUserId(), new com.orderbook.dto.OrderResponse(buyOrder));
        webSocketService.sendOrderUpdate(sellOrder.getUserId(), new com.orderbook.dto.OrderResponse(sellOrder));
        // error message that debugs buyOrder.getUserId() + " " + sellOrder.getUserId());
        // Broadcast order book update

        // Remove filled orders from order book
        if (buyOrder.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0) {
            orderBook.removeOrder(buyOrder.getId());
        }
        if (sellOrder.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0) {
            orderBook.removeOrder(sellOrder.getId());
        }
    }

    private void updateOrderStatus(Order order) {
        if (order.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0) {
            order.setStatus(Order.OrderStatus.FILLED);
        } else if (order.getRemainingQuantity().compareTo(order.getQuantity()) < 0) {
            order.setStatus(Order.OrderStatus.PARTIAL_FILLED);
        }
    }
}
