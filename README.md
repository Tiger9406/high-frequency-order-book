# High-Frequency Order Book

A real-time order book trading system built with Spring Boot (backend) and React TypeScript (frontend).

## Features

- Real-time order book display
- Live trade execution
- WebSocket-based real-time updates
- Order placement (Market and Limit orders)
- Order cancellation
- User order management
- Trade history
- Responsive web interface

## Project Structure

```
high-frequency-order-book/
├── backend/          # Spring Boot application
│   ├── src/main/java/com/orderbook/
│   │   ├── config/   # Configuration classes
│   │   ├── controller/ # REST controllers
│   │   ├── dto/      # Data Transfer Objects
│   │   ├── entity/   # JPA entities
│   │   ├── model/    # Domain models
│   │   ├── repository/ # Data repositories
│   │   └── service/  # Business logic
│   └── src/main/resources/
│       ├── application.properties
│       └── db/migration/ # Flyway migrations
└── frontend/         # React TypeScript application
    └── src/
        ├── components/ # React components
        ├── hooks/    # Custom React hooks
        ├── services/ # API and WebSocket services
        └── types/    # TypeScript definitions
```

## Prerequisites

- Java 17 or higher
- Node.js 16 or higher
- Maven 3.6 or higher
- npm or yarn

## Running the Application

### Backend (Spring Boot)

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Install dependencies and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

3. H2 Database Console (optional):
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:orderbook`
   - Username: `sa`
   - Password: (leave empty)

### Frontend (React)

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

   The frontend will start on `http://localhost:3000`

## API Endpoints

### Orders
- `POST /api/orders` - Place a new order
- `GET /api/orders/user/{userId}` - Get user's orders
- `GET /api/orders/{orderId}` - Get specific order
- `DELETE /api/orders/{orderId}` - Cancel order

### Order Book
- `GET /api/orderbook/{symbol}` - Get order book for symbol
- `GET /api/orderbook/symbols` - Get active symbols

### Trades
- `GET /api/trades/{symbol}` - Get trade history
- `GET /api/trades/{symbol}/stats` - Get trade statistics
- `GET /api/trades/user/{userId}` - Get user's trades

## WebSocket Endpoints

- `/ws` - WebSocket connection endpoint
- `/topic/orderbook/{symbol}` - Order book updates
- `/topic/trades/{symbol}` - Trade updates
- `/topic/orders/{userId}` - User order updates

## Usage

1. Start both backend and frontend applications
2. Open `http://localhost:3000` in your browser
3. Select a trading symbol (BTCUSD, ETHUSD, ADAUSD)
4. Place orders using the order form
5. View real-time order book updates
6. Monitor your orders in the "My Orders" panel
7. See trade history in real-time

## Sample Order Requests

### Limit Order
```json
{
  "symbol": "BTCUSD",
  "side": "BUY",
  "type": "LIMIT",
  "quantity": 0.1,
  "price": 50000,
  "userId": "user123"
}
```

### Market Order
```json
{
  "symbol": "BTCUSD",
  "side": "SELL",
  "type": "MARKET",
  "quantity": 0.05,
  "userId": "user123"
}
```

## Development

### Backend Development
- The application uses H2 in-memory database for development
- Database schema is managed by Flyway migrations
- WebSocket is configured for CORS to allow frontend connections

### Frontend Development
- Uses TypeScript for type safety
- Custom hooks for order book and order submission logic
- WebSocket service for real-time updates
- Responsive design with trading platform styling

## Technologies Used

### Backend
- Spring Boot 3.2
- Spring WebSocket
- Spring Data JPA
- H2 Database
- Flyway
- Maven

### Frontend
- React 18
- TypeScript
- Axios (HTTP client)
- STOMP.js (WebSocket client)
- SockJS

## Production Considerations

For production deployment, consider:
- Replace H2 with a production database (PostgreSQL, MySQL)
- Add authentication and authorization
- Implement proper error handling and logging
- Add monitoring and metrics
- Configure proper CORS settings
- Add rate limiting
- Implement proper order validation
- Add market data feeds
- Consider using Redis for caching
