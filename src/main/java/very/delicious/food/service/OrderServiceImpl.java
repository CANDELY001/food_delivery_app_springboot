package very.delicious.food.service;

import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import very.delicious.food.entities.OrderEntity;
import very.delicious.food.io.OrderRequest;
import very.delicious.food.io.OrderResponse;
import very.delicious.food.repository.OrderRepository;
import com.stripe.model.checkout.Session;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final UserService userService;


    public OrderServiceImpl(OrderRepository orderRepository, PaymentService paymentService, UserService userService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) {
        // 1️⃣ Get logged-in user ID
        String userId = userService.findByUserId();

        // 2️⃣ Convert request to entity
        OrderEntity newOrder = convertToEntity(request);
        newOrder.setUserId(userId);
        newOrder.setPaymentStatus("PENDING");
        newOrder.setOrdersStatus("PENDING");

        // 3️⃣ Save order
        newOrder = orderRepository.save(newOrder);

        try {
            // 4️⃣ Create Stripe Checkout session
            Session session = paymentService.createCheckoutSession(newOrder);
            newOrder.setStripeCheckoutSessionId(session.getId());
            orderRepository.save(newOrder);
        } catch (Exception e) {
            e.printStackTrace();
            newOrder.setPaymentStatus("FAILED");
            orderRepository.save(newOrder);
        }

        return convertToResponse(newOrder);
    }

    @Override
    public List<OrderResponse> getOrdersForUser() {
        String userId = userService.findByUserId();
        List<OrderEntity> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(String orderId) {
        String userId = userService.findByUserId();
        OrderEntity order = orderRepository.findById(orderId)
                .filter(o -> o.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToResponse(order);
    }

    @Override
    public void deleteOrder(String orderId) {
        String userId = userService.findByUserId();
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new AccessDeniedException("You cannot delete this order.");
        }

        orderRepository.delete(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(String orderId, String newStatus) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrdersStatus(newStatus);
        orderRepository.save(order);

        return convertToResponse(order);
    }


    private OrderEntity convertToEntity(OrderRequest orderRequest) {
        return OrderEntity.builder()
                .userAddress(orderRequest.getUserAddress())
                .amount(orderRequest.getAmount())
                .orderedItems(orderRequest.getOrderedItems())
                .email(orderRequest.getEmail())
                .phoneNumber(orderRequest.getPhoneNumber())
                .build();
    }

    private OrderResponse convertToResponse(OrderEntity order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userAddress(order.getUserAddress())
                .phoneNumber(order.getPhoneNumber())
                .userId(order.getUserId())
                .amount(order.getAmount())
                .email(order.getEmail())
                .stripeCheckoutSessionId(order.getStripeCheckoutSessionId())
                .orderedItems(order.getOrderedItems())
                .paymentStatus(order.getPaymentStatus())
                .ordersStatus(order.getOrdersStatus())
                .build();
    }
}
