package very.delicious.food.controller;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import very.delicious.food.entities.OrderEntity;
import very.delicious.food.repository.OrderRepository;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    private final OrderRepository orderRepository;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    public StripeWebhookController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public String handleStripeEvent(HttpServletRequest request,
                                    @RequestHeader("Stripe-Signature") String sigHeader) throws Exception {
        StringBuilder payload = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) payload.append(line);
        }

        Event event = Webhook.constructEvent(payload.toString(), sigHeader, endpointSecret);

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer()
                    .getObject().orElseThrow();
            OrderEntity order = orderRepository.findByStripeCheckoutSessionId(session.getId())
                    .orElseThrow();
            order.setPaymentStatus("PAID");
            orderRepository.save(order);
        }

        return "";
    }

}
