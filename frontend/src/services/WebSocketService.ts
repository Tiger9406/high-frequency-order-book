import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { OrderBookData, Trade, Order } from '../types';

export class WebSocketService {
  private client: Client | null = null;
  private connected = false;

  constructor() {
    this.connect();
  }

  private connect(): void {
    const WS_URL = process.env.REACT_APP_WS_URL || 'http://localhost:8080/ws';
    
    this.client = new Client({
      webSocketFactory: () => new SockJS(WS_URL),
      connectHeaders: {},
      debug: (str) => {
        console.log('STOMP Debug:', str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    this.client.onConnect = (frame) => {
      console.log('Connected to WebSocket:', frame);
      this.connected = true;
    };

    this.client.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    this.client.onWebSocketError = (event) => {
      console.error('WebSocket error:', event);
    };

    this.client.onDisconnect = () => {
      console.log('Disconnected from WebSocket');
      this.connected = false;
    };

    this.client.activate();
  }

  public subscribeToOrderBook(symbol: string, callback: (data: OrderBookData) => void): () => void {
    if (!this.client || !this.connected) {
      console.warn('WebSocket not connected');
      return () => {};
    }

    const subscription = this.client.subscribe(`/topic/orderbook/${symbol}`, (message) => {
      try {
        const data = JSON.parse(message.body);
        callback(data);
      } catch (error) {
        console.error('Error parsing order book data:', error);
      }
    });

    return () => subscription.unsubscribe();
  }

  public subscribeToTrades(symbol: string, callback: (trade: Trade) => void): () => void {
    if (!this.client || !this.connected) {
      console.warn('WebSocket not connected');
      return () => {};
    }

    const subscription = this.client.subscribe(`/topic/trades/${symbol}`, (message) => {
      try {
        const trade = JSON.parse(message.body);
        callback(trade);
      } catch (error) {
        console.error('Error parsing trade data:', error);
      }
    });

    return () => subscription.unsubscribe();
  }

  public subscribeToUserOrders(userId: string, callback: (order: Order) => void): () => void {
    if (!this.client || !this.connected) {
      console.warn('WebSocket not connected');
      return () => {};
    }

    const subscription = this.client.subscribe(`/topic/orders/${userId}`, (message) => {
      try {
        const order = JSON.parse(message.body);
        callback(order);
      } catch (error) {
        console.error('Error parsing order data:', error);
      }
    });

    return () => subscription.unsubscribe();
  }

  public disconnect(): void {
    if (this.client) {
      this.client.deactivate();
    }
  }

  public isConnected(): boolean {
    return this.connected;
  }
}
