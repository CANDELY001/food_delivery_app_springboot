package very.delicious.food.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import very.delicious.food.io.OrderItem;

import java.util.List;

@Data
@Builder
@Document(collection = "orders")
public class OrderEntity {
    @Id
    private String id;
    private String userId;
    private String userAddress;
    private String phoneNumber;
    private String email;
    private List<OrderItem> orderedItems;
    private double amount;
    private String paymentStatus; // pending, paid, failed
    private String stripePaymentIntentId;
    private String stripeCheckoutSessionId; // for web checkout flow
    private String ordersStatus; // pending, confirmed, delivered
}
