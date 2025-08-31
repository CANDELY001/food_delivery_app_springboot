package very.delicious.food.service;

import very.delicious.food.io.OrderRequest;
import very.delicious.food.io.OrderResponse;

import java.util.List;

public interface OrderService {

    /**
     * Create a new order and generate Stripe payment session
     */
    OrderResponse createOrderWithPayment(OrderRequest request);

    /**
     * Get all orders for the logged-in user
     */
    List<OrderResponse> getOrdersForUser();

    /**
     * Get a single order by its ID
     */
    OrderResponse getOrderById(String orderId);

    void deleteOrder(String orderId);

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrderStatus(String orderId, String newStatus);
}
