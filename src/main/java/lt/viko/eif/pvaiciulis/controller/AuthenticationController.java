package lt.viko.eif.pvaiciulis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.dto.request.AuthenticationRequest;
import lt.viko.eif.pvaiciulis.dto.request.RegisterRequest;
import lt.viko.eif.pvaiciulis.dto.response.AuthenticationResponse;
import lt.viko.eif.pvaiciulis.dto.response.RegisterResponse;
import lt.viko.eif.pvaiciulis.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling authentication operations.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    /**
     * Endpoint for registering a new user.
     *
     * @param request The registration request containing user details.
     * @return ResponseEntity with status 201 (Created) and a token on successful registration,
     * or status 400 (Bad Request) with error details if registration fails.
     */
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully registered a new user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Bad request",
                    content  = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponse.class))})
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        RegisterResponse result = service.register(request);

        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthenticationResponse.builder()
                            .error(result.getError())
                            .build());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AuthenticationResponse.builder()
                        .token(result.getToken())
                        .build());
    }

    /**
     * Endpoint for authenticating an existing user.
     *
     * @param request The authentication request containing user credentials.
     * @return ResponseEntity with status 200 (OK) and authentication response on successful authentication,
     * or status 401 (Unauthorized) if authentication fails.
     */
    @Operation(summary = "Authenticate an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated existing user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }
}
