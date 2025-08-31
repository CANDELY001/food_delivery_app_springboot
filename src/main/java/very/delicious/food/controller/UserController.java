package very.delicious.food.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import very.delicious.food.io.UserRequest;
import very.delicious.food.io.UserResponse;
import very.delicious.food.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRequest userRequest) {
        return userService.registerUser(userRequest);
    }
}
