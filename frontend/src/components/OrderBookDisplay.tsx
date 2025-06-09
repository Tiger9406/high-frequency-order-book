import React from 'react';
import { useOrderBook } from '../hooks/useOrderBook';
import { PriceLevel } from '../types';

interface OrderBookDisplayProps {
  symbol: string;
}

const OrderBookDisplay: React.FC<OrderBookDisplayProps> = ({ symbol }) => {
  const { orderBookData, loading, error, refreshData } = useOrderBook(symbol);

  if (loading) {
    return <div className="order-book loading">Loading order book...</div>;
  }

  if (error) {
    return (
      <div className="order-book error">
        <p>Error: {error}</p>
        <button onClick={refreshData}>Retry</button>
      </div>
    );
  }

  if (!orderBookData) {
    return <div className="order-book empty">No order book data available</div>;
  }

  const renderPriceLevel = (level: PriceLevel, side: 'bid' | 'ask') => (
    <div key={level.price} className={`price-level ${side}`}>
      <span className="price">{level.price.toFixed(2)}</span>
      <span className="quantity">{level.quantity.toFixed(8)}</span>
      <span className="order-count">{level.orderCount}</span>
    </div>
  );

  return (
    <div className="order-book">
      <div className="order-book-header">
        <h3>Order Book - {symbol}</h3>
        <button onClick={refreshData} className="refresh-btn">
          Refresh
        </button>
      </div>
      
      <div className="order-book-content">
        <div className="asks-section">
          <div className="section-header">
            <span>Price</span>
            <span>Quantity</span>
            <span>Orders</span>
          </div>
          <div className="asks">
            {orderBookData.asks.slice().reverse().map(level => 
              renderPriceLevel(level, 'ask')
            )}
          </div>
        </div>
        
        <div className="spread-section">
          <div className="spread">
            {orderBookData.asks.length > 0 && orderBookData.bids.length > 0 && (
              <span>
                Spread: {(orderBookData.asks[0].price - orderBookData.bids[0].price).toFixed(2)}
              </span>
            )}
          </div>
        </div>
        
        <div className="bids-section">
          <div className="bids">
            {orderBookData.bids.map(level => 
              renderPriceLevel(level, 'bid')
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderBookDisplay;
