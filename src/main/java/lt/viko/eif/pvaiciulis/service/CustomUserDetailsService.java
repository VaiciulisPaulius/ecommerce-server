package lt.viko.eif.pvaiciulis.service;

import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.model.UserModel.User;
import lt.viko.eif.pvaiciulis.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("customUserDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userId) {
        Integer id = Integer.parseInt(userId);

        Optional<User> optionalUser = repository.findById(id);

        if (optionalUser.isEmpty()) { // Check if Optional is empty
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true, true, user.getAuthorities());
    }
}
