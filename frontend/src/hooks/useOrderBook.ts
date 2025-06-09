import { useState, useEffect, useCallback } from 'react';
import { OrderBookData, Trade } from '../types';
import { ApiService } from '../services/ApiService';
import { WebSocketService } from '../services/WebSocketService';

export const useOrderBook = (symbol: string) => {
  const [orderBookData, setOrderBookData] = useState<OrderBookData | null>(null);
  const [recentTrades, setRecentTrades] = useState<Trade[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [wsService] = useState(() => new WebSocketService());

  const fetchInitialData = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      // Fetch initial order book data
      const orderBook = await ApiService.getOrderBook(symbol);
      setOrderBookData(orderBook);

      // Fetch recent trades
      const trades = await ApiService.getTradeHistory(symbol, 50);
      setRecentTrades(trades);
    } catch (err) {
      setError('Failed to fetch order book data');
      console.error('Error fetching order book data:', err);
    } finally {
      setLoading(false);
    }
  }, [symbol]);

  useEffect(() => {
    fetchInitialData();
  }, [fetchInitialData]);

  useEffect(() => {
    if (!symbol || !wsService.isConnected()) return;

    // Subscribe to order book updates
    const unsubscribeOrderBook = wsService.subscribeToOrderBook(symbol, (data) => {
      setOrderBookData(data);
    });

    // Subscribe to trade updates
    const unsubscribeTrades = wsService.subscribeToTrades(symbol, (trade) => {
      setRecentTrades(prev => [trade, ...prev.slice(0, 49)]); // Keep last 50 trades
    });

    return () => {
      unsubscribeOrderBook();
      unsubscribeTrades();
    };
  }, [symbol, wsService]);

  const refreshData = useCallback(() => {
    fetchInitialData();
  }, [fetchInitialData]);

  return {
    orderBookData,
    recentTrades,
    loading,
    error,
    refreshData,
  };
};
