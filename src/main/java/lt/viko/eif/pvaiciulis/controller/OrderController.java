package lt.viko.eif.pvaiciulis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.dto.request.OrderRequest;
import lt.viko.eif.pvaiciulis.dto.response.OrderResponse;
import lt.viko.eif.pvaiciulis.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Get all orders for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved orders"),
            @ApiResponse(responseCode = "404", description = "No orders found")
    })
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        List<OrderResponse> orders = orderService.getOrdersForUser(userId);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Create a new order for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created order"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request,
            Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        OrderResponse response = orderService.createOrder(userId, request);
        return response.isSuccess() ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @Operation(summary = "Create an order based on the authenticated user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created order from cart"),
            @ApiResponse(responseCode = "400", description = "Error creating order from cart")
    })
    @PostMapping("/from-cart")
    public ResponseEntity<OrderResponse> createOrderFromCart(Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        OrderResponse response = orderService.createOrderFromCart(userId);
        return response.isSuccess() ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @Operation(summary = "Update an existing order for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Integer orderId,
            @RequestBody OrderRequest request,
            Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        OrderResponse response = orderService.updateOrder(userId, orderId, request);
        return response.isSuccess() ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @Operation(summary = "Delete an order for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer orderId, Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        orderService.deleteOrder(userId, orderId);
        return ResponseEntity.noContent().build();
    }
}
