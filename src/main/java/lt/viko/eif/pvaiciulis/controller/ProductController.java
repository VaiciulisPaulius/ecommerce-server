package lt.viko.eif.pvaiciulis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lt.viko.eif.pvaiciulis.dto.request.ProductRequest;
import lt.viko.eif.pvaiciulis.dto.response.ProductResponse;
import lt.viko.eif.pvaiciulis.model.UserModel.Role;
import lt.viko.eif.pvaiciulis.repository.ProductRepository;
import lt.viko.eif.pvaiciulis.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing products. Restricted to users with the role WORKER.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    // Create a new product, only accessible by WORKER role
    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new product",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Unauthorized, WORKER role required",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))})
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest productRequest,
            Authentication authentication) {

        // Get the authenticated user's role
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.WORKER.name()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ProductResponse.builder()
                            .error("Only users with WORKER role can delete products.")
                            .build());
        }

        ProductResponse result = productService.createProduct(productRequest);

        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // Update a product, only accessible by WORKER role
    @Operation(summary = "Update an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the product",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Unauthorized, WORKER role required",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductRequest productRequest,
            Authentication authentication) {

        // Get the authenticated user's role
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.WORKER.name()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ProductResponse.builder()
                            .error("Only users with WORKER role can delete products.")
                            .build());
        }

        // Find the existing product and update it
        ProductResponse result = productService.updateProduct(id, productRequest);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ProductResponse.builder()
                            .error("Product not found.")
                            .success(false)
                            .build());
        }
        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return ResponseEntity.ok(result);
    }

    // Delete a product, only accessible by WORKER role
    @Operation(summary = "Delete a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the product",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Unauthorized, WORKER role required",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(
            @PathVariable Integer id,
            Authentication authentication) {

        // Get the authenticated user's role
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.WORKER.name()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ProductResponse.builder()
                            .error("Only users with WORKER role can delete products.")
                            .build());
        }

        // Call the service method to delete the product
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ProductResponse.builder()
                            .error("Product not found.")
                            .build());
        }

        return ResponseEntity.ok(ProductResponse.builder()
                .id(id)
                .success(true)
                .build());
    }
}

