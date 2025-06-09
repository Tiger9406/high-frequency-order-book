CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(20) NOT NULL,
    side VARCHAR(10) NOT NULL CHECK (side IN ('BUY', 'SELL')),
    type VARCHAR(10) NOT NULL CHECK (type IN ('MARKET', 'LIMIT')),
    quantity DECIMAL(19,8) NOT NULL CHECK (quantity > 0),
    price DECIMAL(19,8),
    remaining_quantity DECIMAL(19,8) NOT NULL CHECK (remaining_quantity >= 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'PARTIAL_FILLED', 'FILLED', 'CANCELLED')),
    user_id VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_orders_symbol_status ON orders(symbol, status);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_symbol_side_price ON orders(symbol, side, price);
CREATE INDEX idx_orders_created_at ON orders(created_at);
