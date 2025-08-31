package very.delicious.food.service;

import very.delicious.food.io.CartRequest;
import very.delicious.food.io.CartResponse;

public interface CartService {
    CartResponse addToCart(CartRequest request);

    CartResponse getCart();

    void clearCart();

    CartResponse removeFromCart(CartRequest request);
}

