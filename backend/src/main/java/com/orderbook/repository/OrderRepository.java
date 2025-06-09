package com.orderbook.repository;

import com.orderbook.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
    
    List<Order> findBySymbolAndStatusOrderByCreatedAtDesc(String symbol, Order.OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.symbol = :symbol AND o.status IN ('PENDING', 'PARTIAL_FILLED') ORDER BY o.createdAt ASC")
    List<Order> findActiveOrdersBySymbol(@Param("symbol") String symbol);
    
    @Query("SELECT o FROM Order o WHERE o.symbol = :symbol AND o.side = :side AND o.status IN ('PENDING', 'PARTIAL_FILLED') ORDER BY " +
           "CASE WHEN :side = 'BUY' THEN o.price END DESC, " +
           "CASE WHEN :side = 'SELL' THEN o.price END ASC, " +
           "o.createdAt ASC")
    List<Order> findActiveOrdersBySymbolAndSide(@Param("symbol") String symbol, @Param("side") Order.OrderSide side);
}
