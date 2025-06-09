import React, { useState } from 'react';
import OrderBookDisplay from './components/OrderBookDisplay';
import OrderForm from './components/OrderForm';
import TradeHistory from './components/TradeHistory';
import UserOrdersPanel from './components/UserOrdersPanel';
import './App.css';

const App: React.FC = () => {
  const [selectedSymbol, setSelectedSymbol] = useState('BTCUSD');
  const [userId] = useState('user123'); // In a real app, this would come from authentication

  return (
    <div className="app">
      <header className="app-header">
        <h1>High-Frequency Order Book</h1>
        <div className="symbol-selector">
          <label htmlFor="symbol">Symbol: </label>
          <select 
            id="symbol" 
            value={selectedSymbol} 
            onChange={(e) => setSelectedSymbol(e.target.value)}
          >
            <option value="BTCUSD">BTC/USD</option>
            <option value="ETHUSD">ETH/USD</option>
            <option value="ADAUSD">ADA/USD</option>
          </select>
        </div>
      </header>

      <main className="app-main">
        <div className="trading-grid">
          <div className="order-book-section">
            <OrderBookDisplay symbol={selectedSymbol} />
          </div>
          
          <div className="order-form-section">
            <OrderForm symbol={selectedSymbol} userId={userId} />
          </div>
          
          <div className="trade-history-section">
            <TradeHistory symbol={selectedSymbol} />
          </div>
          
          <div className="user-orders-section">
            <UserOrdersPanel userId={userId} />
          </div>
        </div>
      </main>
    </div>
  );
};

export default App;
