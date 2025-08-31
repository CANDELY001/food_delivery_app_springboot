package very.delicious.food.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import very.delicious.food.entities.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {
    List<OrderEntity> findByUserId(String userId);
    Optional<OrderEntity> findByStripePaymentIntentId(String stripePaymentIntentId);

    Optional<OrderEntity> findByStripeCheckoutSessionId(String stripeCheckoutSessionId);
}
