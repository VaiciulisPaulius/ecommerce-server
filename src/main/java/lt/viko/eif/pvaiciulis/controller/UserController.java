package lt.viko.eif.pvaiciulis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.dto.response.UserResponse;
import lt.viko.eif.pvaiciulis.exception.ResourceNotFoundException;
import lt.viko.eif.pvaiciulis.model.UserModel.Role;
import lt.viko.eif.pvaiciulis.model.UserModel.User;
import lt.viko.eif.pvaiciulis.modelassembler.UserModelAssembler;
import lt.viko.eif.pvaiciulis.repository.UserRepository;
import lt.viko.eif.pvaiciulis.service.UserService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class that manages user-related operations.
 */
@RestController
@RequestMapping("/api/v1/auth/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserModelAssembler userModelAssembler;
    private final UserService userService;

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return ResponseEntity with status 200 (OK) and the retrieved user on success.
     */
    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id, Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());

        User admin = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User who called not found"));

        if(admin.getRole() != Role.ADMIN) throw new AccessDeniedException("Cant access this resource.");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword("");

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<UserResponse> getUser(Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword("");

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                //.address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody User user, Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());

        User authenticatedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User updatedUser = userService.updateUser(userId, user, authenticatedUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
