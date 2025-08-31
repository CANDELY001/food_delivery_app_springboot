package very.delicious.food.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import very.delicious.food.io.OrderRequest;
import very.delicious.food.io.OrderResponse;
import very.delicious.food.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;



    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create a new order and generate Stripe Checkout session
     */
    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.createOrderWithPayment(orderRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all orders for the logged-in user
     */
    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<OrderResponse>> getUserOrders() {
        List<OrderResponse> orders = orderService.getOrdersForUser(); // implement in service
        return ResponseEntity.ok(orders);
    }

    /**
     * Get order by ID (optional)
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String orderId) {
        OrderResponse order = orderService.getOrderById(orderId); // implement in service
        return ResponseEntity.ok(order);
    }
    /**
     * Remove an order by ID (logged-in user)
     */
    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> removeOrder(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted successfully");
    }

    /**
     * Get all users' orders (admin endpoint)
     */
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getAllUsersOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/status/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam String newStatus ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId,  newStatus));
    }


}
