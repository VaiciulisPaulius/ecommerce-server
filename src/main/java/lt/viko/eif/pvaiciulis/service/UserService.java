package lt.viko.eif.pvaiciulis.service;
import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.exception.ResourceNotFoundException;
import lt.viko.eif.pvaiciulis.exception.UnauthorizedException;
import lt.viko.eif.pvaiciulis.model.UserModel.User;
import lt.viko.eif.pvaiciulis.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user operations and interacting with UserRepository.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the User object corresponding to the ID
     */
    public User findByUsername(String id) {
        Integer userId = Integer.parseInt(id);

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public User updateUser(Integer id, User user, User authenticatedUser) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!authenticatedUser.equals(updatedUser)) {
            throw new UnauthorizedException("Unauthorized");
        }

        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPhoneNumber(user.getPhoneNumber());

        return userRepository.save(updatedUser);
    }
}
