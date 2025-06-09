CREATE TABLE trades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(20) NOT NULL,
    buy_order_id BIGINT NOT NULL,
    sell_order_id BIGINT NOT NULL,
    buy_user_id VARCHAR(50) NOT NULL,
    sell_user_id VARCHAR(50) NOT NULL,
    quantity DECIMAL(19,8) NOT NULL CHECK (quantity > 0),
    price DECIMAL(19,8) NOT NULL CHECK (price > 0),
    executed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (buy_order_id) REFERENCES orders(id),
    FOREIGN KEY (sell_order_id) REFERENCES orders(id)
);

CREATE INDEX idx_trades_symbol ON trades(symbol);
CREATE INDEX idx_trades_executed_at ON trades(executed_at);
CREATE INDEX idx_trades_buy_user_id ON trades(buy_user_id);
CREATE INDEX idx_trades_sell_user_id ON trades(sell_user_id);
CREATE INDEX idx_trades_symbol_executed_at ON trades(symbol, executed_at);
