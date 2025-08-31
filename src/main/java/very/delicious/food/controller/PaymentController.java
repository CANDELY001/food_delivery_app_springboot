package very.delicious.food.controller;

import com.stripe.model.checkout.Session;
import org.springframework.web.bind.annotation.*;
import very.delicious.food.entities.OrderEntity;
import very.delicious.food.repository.OrderRepository;
import very.delicious.food.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;

    public PaymentController(PaymentService paymentService, OrderRepository orderRepository) {
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/create-checkout-session/{orderId}")
    public String createCheckoutSession(@PathVariable String orderId) throws Exception {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        Session session = paymentService.createCheckoutSession(order);
        orderRepository.save(order);
        return session.getUrl(); // frontend redirects to this
    }

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("session_id") String sessionId) throws Exception {
        OrderEntity order = orderRepository.findByStripeCheckoutSessionId(sessionId).orElseThrow();
        order.setPaymentStatus("PAID");
        orderRepository.save(order);
        return "Payment successful!";
    }

    @GetMapping("/cancel")
    public String paymentCancelled() {
        return "Payment cancelled!";
    }
}

