package very.delicious.food.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import very.delicious.food.entities.UserEntity;
import very.delicious.food.repository.UserRepository;

import java.util.Collections;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;


   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
