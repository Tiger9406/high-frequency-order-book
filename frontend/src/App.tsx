import React, { useState, useEffect } from "react";
import OrderBookDisplay from "./components/OrderBookDisplay";
import OrderForm from "./components/OrderForm";
import TradeHistory from "./components/TradeHistory";
import UserOrdersPanel from "./components/UserOrdersPanel";
import "./App.css";

// Generate a unique user ID for this browser tab
const generateUserId = (): string => {
  const timestamp = Date.now();
  const random = Math.random().toString(36).substring(2, 8);
  return `user_${timestamp}_${random}`;
};

const App: React.FC = () => {
  const [selectedSymbol, setSelectedSymbol] = useState("BTCUSD");
  const [userId, setUserId] = useState<string>("");
  const [isUserIdLoading, setIsUserIdLoading] = useState(true);

  // Generate unique user ID for this tab on component mount
  useEffect(() => {
    // Check if we already have a user ID in sessionStorage (for this tab only)
    let tabUserId = sessionStorage.getItem("userTabId");

    if (!tabUserId) {
      // Generate new user ID for this tab
      tabUserId = generateUserId();
      sessionStorage.setItem("userTabId", tabUserId);
    }

    setUserId(tabUserId);
    setIsUserIdLoading(false);
  }, []);

  // Don't render the app until we have a user ID
  if (isUserIdLoading) {
    return (
      <div className="app loading">
        <div className="loading-spinner">Loading...</div>
      </div>
    );
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>High-Frequency Order Book</h1>
        <div className="header-controls">
          <div className="user-info">
            <span className="user-label">User:</span>
            <span className="user-id">{userId}</span>
          </div>
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
