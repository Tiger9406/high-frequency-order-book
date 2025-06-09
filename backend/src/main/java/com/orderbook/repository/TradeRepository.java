package com.orderbook.repository;

import com.orderbook.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    
    List<Trade> findBySymbolOrderByExecutedAtDesc(String symbol);
    
    List<Trade> findTop100BySymbolOrderByExecutedAtDesc(String symbol);
    
    @Query("SELECT t FROM Trade t WHERE t.symbol = :symbol AND t.executedAt >= :since ORDER BY t.executedAt DESC")
    List<Trade> findBySymbolAndExecutedAtAfter(@Param("symbol") String symbol, @Param("since") LocalDateTime since);
    
    @Query("SELECT t FROM Trade t WHERE (t.buyUserId = :userId OR t.sellUserId = :userId) ORDER BY t.executedAt DESC")
    List<Trade> findByUserId(@Param("userId") String userId);
}
