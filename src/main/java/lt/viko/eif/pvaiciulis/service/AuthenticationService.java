package lt.viko.eif.pvaiciulis.service;

import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.dto.response.AuthenticationResponse;
import lt.viko.eif.pvaiciulis.dto.request.AuthenticationRequest;
import lt.viko.eif.pvaiciulis.dto.request.RegisterRequest;
import lt.viko.eif.pvaiciulis.dto.response.RegisterResponse;
import lt.viko.eif.pvaiciulis.exception.ResourceNotFoundException;
import lt.viko.eif.pvaiciulis.model.UserModel.Address;
import lt.viko.eif.pvaiciulis.model.CartModel.Cart;
import lt.viko.eif.pvaiciulis.model.UserModel.Role;
import lt.viko.eif.pvaiciulis.model.UserModel.User;
import lt.viko.eif.pvaiciulis.repository.AddressRepository;
import lt.viko.eif.pvaiciulis.repository.CartRepository;
import lt.viko.eif.pvaiciulis.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service class handling authentication operations.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user with the provided registration details.
     *
     * @param request Registration request containing user details (firstname, lastname, email, password).
     * @return RegisterResponse indicating success or failure of the registration attempt.
     */
    public RegisterResponse register(RegisterRequest request) {
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        System.out.println(request.getRole());
        if (userRepository.existsByEmail(request.getEmail())) {
            return RegisterResponse.builder()
                    .success(false)
                    .error("Email already registered")
                    .build();
        }
        if(request.getEmail().isBlank()){
            return RegisterResponse.builder()
                    .success(false)
                    .error("Email is empty")
                    .build();
        }
        if(request.getPassword().isBlank()){
            return RegisterResponse.builder()
                    .success(false)
                    .error("Password is empty")
                    .build();
        }
        if(request.getPhoneNumber().isBlank()){
            return RegisterResponse.builder()
                    .success(false)
                    .error("Phone number not provided")
                    .build();
        }
        if(request.getRole() == null || request.getRole().toString().isBlank()){
            request.setRole(Role.USER);
        }

        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .phoneNumber(request.getPhoneNumber())
                .build();
        userRepository.save(user);

        var cart = Cart.builder()
                .user(user)
                .build();
        cartRepository.save(cart);

        var address = Address.builder()
                .user(user)
                .build();
        addressRepository.save(address);

        var jwtToken = jwtService.generateToken(user);
        return RegisterResponse.builder()
                .success(true)
                .token(jwtToken)
                .build();
    }

    /**
     * Authenticates a user based on the provided credentials (email and password).
     *
     * @param request Authentication request containing user email and password.
     * @return AuthenticationResponse containing JWT token upon successful authentication.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        System.out.println("Authenticating...");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            System.out.println("Fail...");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    public User requireRole(Authentication authentication, Role requiredRole) {
        Integer userId = Integer.parseInt(authentication.getName());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        if (user.getRole() != requiredRole) {
            throw new AccessDeniedException("You don't have permission to access this resource.");
        }

        return user;
    }

    public User getCurrentUser(Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

}
