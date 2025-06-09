import React, { useState } from 'react';
import { useOrderSubmission } from '../hooks/useOrderSubmission';
import { OrderRequest } from '../types';

interface OrderFormProps {
  symbol: string;
  userId: string;
}

const OrderForm: React.FC<OrderFormProps> = ({ symbol, userId }) => {
  const [side, setSide] = useState<'BUY' | 'SELL'>('BUY');
  const [type, setType] = useState<'MARKET' | 'LIMIT'>('LIMIT');
  const [quantity, setQuantity] = useState('');
  const [price, setPrice] = useState('');
  
  const { submitOrder, submitting, error, clearError } = useOrderSubmission();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    clearError();

    const orderRequest: OrderRequest = {
      symbol,
      side,
      type,
      quantity: parseFloat(quantity),
      price: type === 'LIMIT' ? parseFloat(price) : undefined,
      userId,
    };

    const order = await submitOrder(orderRequest);
    if (order) {
      // Reset form on success
      setQuantity('');
      if (type === 'LIMIT') {
        setPrice('');
      }
    }
  };

  return (
    <div className="order-form">
      <h3>Place Order</h3>
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Symbol</label>
          <input type="text" value={symbol} readOnly />
        </div>

        <div className="form-group">
          <label>Side</label>
          <div className="button-group">
            <button
              type="button"
              className={side === 'BUY' ? 'active buy' : 'buy'}
              onClick={() => setSide('BUY')}
            >
              Buy
            </button>
            <button
              type="button"
              className={side === 'SELL' ? 'active sell' : 'sell'}
              onClick={() => setSide('SELL')}
            >
              Sell
            </button>
          </div>
        </div>

        <div className="form-group">
          <label>Type</label>
          <div className="button-group">
            <button
              type="button"
              className={type === 'MARKET' ? 'active' : ''}
              onClick={() => setType('MARKET')}
            >
              Market
            </button>
            <button
              type="button"
              className={type === 'LIMIT' ? 'active' : ''}
              onClick={() => setType('LIMIT')}
            >
              Limit
            </button>
          </div>
        </div>

        <div className="form-group">
          <label>Quantity</label>
          <input
            type="number"
            step="0.00000001"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value)}
            required
            min="0"
          />
        </div>

        {type === 'LIMIT' && (
          <div className="form-group">
            <label>Price</label>
            <input
              type="number"
              step="0.01"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              required
              min="0"
            />
          </div>
        )}

        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        <button
          type="submit"
          disabled={submitting || !quantity || (type === 'LIMIT' && !price)}
          className={`submit-btn ${side.toLowerCase()}`}
        >
          {submitting ? 'Placing Order...' : `${side} ${symbol}`}
        </button>
      </form>
    </div>
  );
};

export default OrderForm;
