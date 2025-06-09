import { useState, useCallback } from 'react';
import { OrderRequest, Order } from '../types';
import { ApiService } from '../services/ApiService';

export const useOrderSubmission = () => {
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const submitOrder = useCallback(async (orderRequest: OrderRequest): Promise<Order | null> => {
    try {
      setSubmitting(true);
      setError(null);

      // Validate order request
      if (!orderRequest.symbol || !orderRequest.side || !orderRequest.type || !orderRequest.quantity) {
        throw new Error('Missing required order fields');
      }

      if (orderRequest.quantity <= 0) {
        throw new Error('Quantity must be greater than 0');
      }

      if (orderRequest.type === 'LIMIT' && (!orderRequest.price || orderRequest.price <= 0)) {
        throw new Error('Limit orders must have a valid price');
      }

      const order = await ApiService.placeOrder(orderRequest);
      return order;
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Failed to submit order';
      setError(errorMessage);
      console.error('Error submitting order:', err);
      return null;
    } finally {
      setSubmitting(false);
    }
  }, []);

  const cancelOrder = useCallback(async (orderId: number, userId: string): Promise<boolean> => {
    try {
      setSubmitting(true);
      setError(null);

      await ApiService.cancelOrder(orderId, userId);
      return true;
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Failed to cancel order';
      setError(errorMessage);
      console.error('Error canceling order:', err);
      return false;
    } finally {
      setSubmitting(false);
    }
  }, []);

  const clearError = useCallback(() => {
    setError(null);
  }, []);

  return {
    submitOrder,
    cancelOrder,
    submitting,
    error,
    clearError,
  };
};
