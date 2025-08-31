package very.delicious.food.service;


import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import very.delicious.food.entities.UserEntity;
import very.delicious.food.io.UserRequest;
import very.delicious.food.io.UserResponse;
import very.delicious.food.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationFacade authenticationFacade) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        UserEntity newUser = convertToEntity(userRequest);
        newUser = userRepository.save(newUser);
        return convertToResponse(newUser);
    }

    @Override
    public String findByUserId() {
       String loggedInUserEmail = authenticationFacade.getAuthentication().getName();
       UserEntity loggedInUser = userRepository.findByEmail(loggedInUserEmail).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
        return loggedInUser.getId();
    }



    private UserEntity convertToEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .name(userRequest.getName())
                .build();
    }

    private UserResponse convertToResponse(UserEntity registeredUser) {
        return UserResponse.builder()
                .id(registeredUser.getId())
                .name(registeredUser.getName())
                .email(registeredUser.getEmail())
                .build();
    }
}
