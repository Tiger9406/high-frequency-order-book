import React, { useState, useEffect, useCallback } from "react";
import { Order } from "../types";
import { ApiService } from "../services/ApiService";
import { useOrderSubmission } from "../hooks/useOrderSubmission";
import { WebSocketService } from "../services/WebSocketService";

interface UserOrdersPanelProps {
  userId: string;
}

const UserOrdersPanel: React.FC<UserOrdersPanelProps> = ({ userId }) => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { cancelOrder, submitting } = useOrderSubmission();
  const [wsService] = useState(() => new WebSocketService());

  const fetchUserOrders = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const userOrders = await ApiService.getUserOrders(userId);
      setOrders(userOrders);
    } catch (err) {
      setError("Failed to fetch user orders");
      console.error("Error fetching user orders:", err);
    } finally {
      setLoading(false);
    }
  }, [userId]);

  useEffect(() => {
    fetchUserOrders();
  }, [fetchUserOrders]);

  useEffect(() => {
    if (!userId) return;

    let unsubscribe: (() => void) | null = null;

    const setupSubscription = () => {
      unsubscribe = wsService.subscribeToUserOrders(userId, (updatedOrder) => {
        setOrders((prev) => {
          const index = prev.findIndex((order) => order.id === updatedOrder.id);
          if (index >= 0) {
            const newOrders = [...prev];
            newOrders[index] = updatedOrder;
            return newOrders;
          } else {
            return [updatedOrder, ...prev];
          }
        });
      });
    };

    // Set up subscription when connected
    const removeConnectionListener = wsService.onConnection(setupSubscription);

    return () => {
      removeConnectionListener();
      if (unsubscribe) unsubscribe();
    };
  }, [userId, wsService]);

  const handleCancelOrder = async (orderId: number) => {
    const success = await cancelOrder(orderId, userId);
    if (success) {
      await fetchUserOrders(); // Refresh the list
    }
  };

  /*const formatDateTime = (timestamp: string) => {
    return new Date(timestamp).toLocaleString();
  };*/

  const formatDateTime = (timestamp: string) => {
    const dateObj = new Date(timestamp);
    return {
      date: dateObj.toLocaleDateString(),
      time: dateObj.toLocaleTimeString(),
    };
  };
  

  const formatPrice = (price?: number) => {
    return price ? price.toFixed(2) : "Market";
  };

  const formatQuantity = (quantity: number) => {
    return quantity.toFixed(8);
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "FILLED":
        return "green";
      case "CANCELLED":
        return "red";
      case "PARTIAL_FILLED":
        return "orange";
      default:
        return "blue";
    }
  };

  const canCancelOrder = (order: Order) => {
    return order.status === "PENDING" || order.status === "PARTIAL_FILLED";
  };

  if (loading) {
    return <div className="user-orders loading">Loading orders...</div>;
  }

  if (error) {
    return (
      <div className="user-orders error">
        <p>Error: {error}</p>
        <button onClick={fetchUserOrders}>Retry</button>
      </div>
    );
  }

  return (
    <div className="user-orders">
      <div className="user-orders-header">
        <h3>My Orders</h3>
        <button onClick={fetchUserOrders} className="refresh-btn">
          Refresh
        </button>
      </div>

      <div className="orders-container">
        {orders.length === 0 ? (
          <div className="no-orders">No orders found</div>
        ) : (
          <div className="orders-list">
            {orders.map((order) => (
              <div key={order.id} className="order-item">
                <div className="order-header">
                  <span className={`order-side ${order.side.toLowerCase()}`}>
                    {order.side}
                  </span>
                  <span className="order-symbol">{order.symbol}</span>
                  <span
                    className="order-status"
                    style={{ color: getStatusColor(order.status) }}
                  >
                    {order.status}
                  </span>
                </div>

                <div className="order-details">
                  <div className="order-row">
                    <span>Type: </span>
                    <span>{order.type}</span>
                  </div>
                  <div className="order-row">
                    <span>Price: </span>
                    <span>{formatPrice(order.price)}</span>
                  </div>
                  <div className="order-row">
                    <span>Quantity: </span>
                    <span>{formatQuantity(order.quantity)}</span>
                  </div>
                  <div className="order-row">
                    <span>Remaining: </span>
                    <span>{formatQuantity(order.remainingQuantity)}</span>
                  </div>
                 { /*<div className="order-row">
                    <span>Created:</span>
                    <span>{formatDateTime(order.createdAt)}</span>
                 </div>*/}
                  <div className="order-row">
  <span>Created:<br></br> </span>
  <span>
    {formatDateTime(order.createdAt).date}
    <br />
    {formatDateTime(order.createdAt).time}
  </span>
</div>

                </div>

                {canCancelOrder(order) && (
                  <div className="order-actions">
                    <button
                      onClick={() => handleCancelOrder(order.id)}
                      disabled={submitting}
                      className="cancel-btn"
                    >
                      {submitting ? "Cancelling..." : "Cancel"}
                    </button>
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default UserOrdersPanel;
