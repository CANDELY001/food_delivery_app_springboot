package very.delicious.food.service;

import org.springframework.stereotype.Service;
import very.delicious.food.entities.CartEntity;
import very.delicious.food.io.CartRequest;
import very.delicious.food.io.CartResponse;
import very.delicious.food.repository.CartRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private final UserService userService;

    public CartServiceImpl(UserService userService, CartRepository cartRepository) {
        this.userService = userService;
        this.cartRepository= cartRepository;
    }

    @Override
    public CartResponse addToCart(CartRequest request) {
        String loggedInUserId = userService.findByUserId();
        Optional<CartEntity> cartOptional = cartRepository.findByUserId(loggedInUserId);
        CartEntity entity = cartOptional.orElseGet(() -> new CartEntity(loggedInUserId, new HashMap<>()));
        Map<String, Integer> cartItems = entity.getItems();
        cartItems.put(request.getFoodId(), cartItems.getOrDefault(request.getFoodId(), 0) + 1);
        entity.setItems(cartItems);
        entity = cartRepository.save(entity);
        return convertToResponse(entity);
    }

    @Override
    public CartResponse getCart() {
      String loggedInUserId = userService.findByUserId();
      CartEntity entity = cartRepository.findByUserId(loggedInUserId).orElse(new CartEntity(loggedInUserId, new HashMap<>()));
     return convertToResponse(entity);
    }

    @Override
    public void clearCart() {
        String loggedInUserId = userService.findByUserId();
        cartRepository.deleteByUserId(loggedInUserId);
    }

    @Override
    public CartResponse removeFromCart(CartRequest request) {
        String loggedInUserId = userService.findByUserId();
        CartEntity entity = cartRepository.findByUserId(loggedInUserId).orElseThrow(()-> new RuntimeException("Cart is not found"));
        Map<String, Integer> cartItems = entity.getItems();
        if(cartItems.containsKey(request.getFoodId())) {
          int currentCount = cartItems.get(request.getFoodId());
          if(currentCount > 0) {
              cartItems.put(request.getFoodId(), currentCount - 1);
          } else {
              cartItems.remove(request.getFoodId());
        }
         entity =  cartRepository.save(entity);

    } return convertToResponse(entity);
    }

    private CartResponse convertToResponse(CartEntity cartEntity) {
        return CartResponse.builder()
                .id(cartEntity.getId())
                .userId(cartEntity.getUserId())
                .items(cartEntity.getItems())
                .build();
    }


}
