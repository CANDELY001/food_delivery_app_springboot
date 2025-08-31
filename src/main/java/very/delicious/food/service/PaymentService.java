package very.delicious.food.service;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;
import very.delicious.food.entities.OrderEntity;

@Service
public class PaymentService {

    public Session createCheckoutSession(OrderEntity order) throws Exception {
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/payment/success?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl("http://localhost:8080/payment/cancel")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("usd")
                                                        .setUnitAmount((long) (order.getAmount() * 100)) // cents
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("Food Order")
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        Session session = Session.create(params);

        order.setStripeCheckoutSessionId(session.getId());
        order.setPaymentStatus("PENDING");

        return session;
    }
}
