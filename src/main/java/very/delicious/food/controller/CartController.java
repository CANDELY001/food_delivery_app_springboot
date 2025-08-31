package very.delicious.food.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import very.delicious.food.io.CartRequest;
import very.delicious.food.io.CartResponse;
import very.delicious.food.service.CartService;
import very.delicious.food.service.CartServiceImpl;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public CartResponse addToCart(@RequestBody CartRequest request) {
        String foodId = request.getFoodId();
        if(foodId == null || foodId.isEmpty()){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "foodId is found");
        }
       return cartService.addToCart(request);
    }

    @GetMapping
    public CartResponse getCart(){
        return cartService.getCart();
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(){
        cartService.clearCart();
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CartResponse removeFromCart(@RequestBody CartRequest request){
        String foodId = request.getFoodId();
        if(foodId == null || foodId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "foodId is found");
        }
        return cartService.removeFromCart(request);
    }
}
