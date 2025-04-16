package lt.viko.eif.pvaiciulis.service;

import lt.viko.eif.pvaiciulis.exception.UnauthorizedException;
import lt.viko.eif.pvaiciulis.model.UserModel.User;
import lt.viko.eif.pvaiciulis.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testFindByUsernameSuccess() {
        User user = User.builder().id(1).build();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.findByUsername("1");
        assertEquals(user, result);
    }

    @Test
    public void testFindByUsernameNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findByUsername("1"));
    }

    @Test
    public void testUpdateUserSuccess() {
        User user = User.builder().id(1).firstName("John").build();
        User updatedUser = User.builder().id(1).firstName("Johnny").build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1, updatedUser, user);
        assertEquals("Johnny", result.getFirstName());
    }

    @Test
    public void testUpdateUserUnauthorized() {
        User user = User.builder().id(1).build();
        User anotherUser = User.builder().id(2).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedException.class, () -> userService.updateUser(1, user, anotherUser));
    }
}