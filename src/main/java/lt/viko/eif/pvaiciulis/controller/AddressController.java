package lt.viko.eif.pvaiciulis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.dto.request.AddressRequest;
import lt.viko.eif.pvaiciulis.dto.response.AddressResponse;
import lt.viko.eif.pvaiciulis.model.UserModel.User;
import lt.viko.eif.pvaiciulis.service.AddressService;
import lt.viko.eif.pvaiciulis.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Get the address for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved address"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @GetMapping
    public ResponseEntity<AddressResponse> getAddress(Authentication authentication) {
        User user = authenticationService.getCurrentUser(authentication);


        AddressResponse response = addressService.getAddressForUser(user.getId());
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @Operation(summary = "Create or update the address for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created/updated address"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<AddressResponse> createOrUpdateAddress(
            @RequestBody AddressRequest request,
            Authentication authentication) {
        User user = authenticationService.getCurrentUser(authentication);

        AddressResponse response = addressService.createOrUpdateAddress(user.getId(), request);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @Operation(summary = "Delete the address for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted address"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteAddress(Authentication authentication) {
        User user = authenticationService.getCurrentUser(authentication);
        addressService.deleteAddress(user.getId());
        return ResponseEntity.noContent().build();
    }
}

