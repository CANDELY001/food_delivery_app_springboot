package very.delicious.food.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import very.delicious.food.io.UserRequest;
import very.delicious.food.io.UserResponse;


public interface UserService {
    UserResponse registerUser(UserRequest userRequest);

    String findByUserId();


}
