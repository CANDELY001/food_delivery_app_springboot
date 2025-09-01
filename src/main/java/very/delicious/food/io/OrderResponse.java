package very.delicious.food.io;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderResponse {
    private String id;
    private String userId;
    private String userAddress;
    private String phoneNumber;
    private String email;
    private double amount;
    private String stripeCheckoutSessionId;
    private List<OrderItem> orderedItems;
    private String paymentStatus; // pending, paid, failed
    private String ordersStatus; // pending, confirmed, delivered
}
