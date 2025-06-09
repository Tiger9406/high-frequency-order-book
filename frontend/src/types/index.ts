export interface Order {
  id: number;
  symbol: string;
  side: 'BUY' | 'SELL';
  type: 'MARKET' | 'LIMIT';
  quantity: number;
  price?: number;
  remainingQuantity: number;
  status: 'PENDING' | 'PARTIAL_FILLED' | 'FILLED' | 'CANCELLED';
  userId: string;
  createdAt: string;
}

export interface Trade {
  id: number;
  symbol: string;
  buyOrderId: number;
  sellOrderId: number;
  buyUserId: string;
  sellUserId: string;
  quantity: number;
  price: number;
  executedAt: string;
}

export interface PriceLevel {
  price: number;
  quantity: number;
  orderCount: number;
}

export interface OrderBookData {
  symbol: string;
  bids: PriceLevel[];
  asks: PriceLevel[];
  timestamp: number;
}

export interface OrderRequest {
  symbol: string;
  side: 'BUY' | 'SELL';
  type: 'MARKET' | 'LIMIT';
  quantity: number;
  price?: number;
  userId: string;
}

export interface TradeStats {
  symbol: string;
  lastPrice?: number;
  volume24h: number;
  priceChange24h: number;
  high24h?: number;
  low24h?: number;
  tradeCount24h: number;
}
