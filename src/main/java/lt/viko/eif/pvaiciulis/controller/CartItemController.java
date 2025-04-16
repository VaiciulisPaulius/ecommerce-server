package lt.viko.eif.pvaiciulis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.dto.request.CartItemRequest;
import lt.viko.eif.pvaiciulis.dto.response.CartItemResponse;
import lt.viko.eif.pvaiciulis.model.UserModel.User;
import lt.viko.eif.pvaiciulis.service.AuthenticationService;
import lt.viko.eif.pvaiciulis.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class to manage cart items for authenticated users.
 */
@RestController
@RequestMapping("/api/v1/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;
    private final AuthenticationService authenticationService;

    /**
     * Retrieves all cart items for the authenticated user.
     */
    @Operation(summary = "Get all cart items for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cart items"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItems(Authentication authentication) {
        User user = authenticationService.getCurrentUser(authentication);
        List<CartItemResponse> cartItemsResponse = cartItemService.getCartItemsForUser(user.getId());
        return new ResponseEntity<>(cartItemsResponse, HttpStatus.OK);
    }

    /**
     * Adds a new cart item for the authenticated user.
     */
    @Operation(summary = "Add a new cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added cart item"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<CartItemResponse> addCartItem(@RequestBody CartItemRequest request, Authentication authentication) {
        User user = authenticationService.getCurrentUser(authentication);
        CartItemResponse response = cartItemService.createCartItem(user.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Updates an existing cart item for the authenticated user.
     */
    @Operation(summary = "Update a cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated cart item"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @PutMapping("")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @RequestBody CartItemRequest request,
            Authentication authentication) {

        User user = authenticationService.getCurrentUser(authentication);
        CartItemResponse response = cartItemService.updateCartItemForUser(user.getId(), request);

        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a cart item for the authenticated user.
     */
    @Operation(summary = "Delete a cart item based on product id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted cart item"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Integer id, Authentication authentication) {
        User user = authenticationService.getCurrentUser(authentication);
        cartItemService.deleteCartItemForUser(user.getId(), id);
        return ResponseEntity.noContent().build();
    }
}

