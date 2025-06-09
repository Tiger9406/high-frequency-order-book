import axios from 'axios';
import { Order, OrderRequest, Trade, OrderBookData, TradeStats } from '../types';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: `${API_BASE_URL}/api`,
  headers: {
    'Content-Type': 'application/json',
  },
});

export class ApiService {
  // Order endpoints
  static async placeOrder(orderRequest: OrderRequest): Promise<Order> {
    const response = await api.post('/orders', orderRequest);
    return response.data;
  }

  static async getUserOrders(userId: string): Promise<Order[]> {
    const response = await api.get(`/orders/user/${userId}`);
    return response.data;
  }

  static async cancelOrder(orderId: number, userId: string): Promise<void> {
    await api.delete(`/orders/${orderId}?userId=${userId}`);
  }

  static async getOrder(orderId: number): Promise<Order> {
    const response = await api.get(`/orders/${orderId}`);
    return response.data;
  }

  // Order book endpoints
  static async getOrderBook(symbol: string, depth: number = 20): Promise<OrderBookData> {
    const response = await api.get(`/orderbook/${symbol}?depth=${depth}`);
    return response.data;
  }

  static async getActiveSymbols(): Promise<string[]> {
    const response = await api.get('/orderbook/symbols');
    return response.data;
  }

  // Trade endpoints
  static async getTradeHistory(symbol: string, limit: number = 100): Promise<Trade[]> {
    const response = await api.get(`/trades/${symbol}?limit=${limit}`);
    return response.data;
  }

  static async getTradeStats(symbol: string): Promise<TradeStats> {
    const response = await api.get(`/trades/${symbol}/stats`);
    return response.data;
  }

  static async getUserTrades(userId: string): Promise<Trade[]> {
    const response = await api.get(`/trades/user/${userId}`);
    return response.data;
  }
}
