import React from 'react';
import { useOrderBook } from '../hooks/useOrderBook';
import { Trade } from '../types';

interface TradeHistoryProps {
  symbol: string;
}

const TradeHistory: React.FC<TradeHistoryProps> = ({ symbol }) => {
  const { recentTrades, loading } = useOrderBook(symbol);

  if (loading) {
    return <div className="trade-history loading">Loading trades...</div>;
  }

  const formatTime = (timestamp: string) => {
    return new Date(timestamp).toLocaleTimeString();
  };

  const formatPrice = (price: number) => {
    return price.toFixed(2);
  };

  const formatQuantity = (quantity: number) => {
    return quantity.toFixed(8);
  };

  return (
    <div className="trade-history">
      <h3>Recent Trades - {symbol}</h3>
      
      <div className="trades-container">
        <div className="trades-header">
          <span>Time</span>
          <span>Price</span>
          <span>Quantity</span>
        </div>
        
        <div className="trades-list">
          {recentTrades.length === 0 ? (
            <div className="no-trades">No recent trades</div>
          ) : (
            recentTrades.map((trade: Trade) => (
              <div key={trade.id} className="trade-item">
                <span className="time">{formatTime(trade.executedAt)}</span>
                <span className="price">{formatPrice(trade.price)}</span>
                <span className="quantity">{formatQuantity(trade.quantity)}</span>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default TradeHistory;
