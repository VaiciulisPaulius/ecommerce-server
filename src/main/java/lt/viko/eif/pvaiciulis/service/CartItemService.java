package lt.viko.eif.pvaiciulis.service;

import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.dto.request.CartItemRequest;
import lt.viko.eif.pvaiciulis.dto.response.CartItemResponse;
import lt.viko.eif.pvaiciulis.dto.response.CartProductResponse;
import lt.viko.eif.pvaiciulis.dto.response.ProductResponse;
import lt.viko.eif.pvaiciulis.exception.ResourceNotFoundException;
import lt.viko.eif.pvaiciulis.model.CartModel.Cart;
import lt.viko.eif.pvaiciulis.model.CartModel.CartItem;
import lt.viko.eif.pvaiciulis.model.ProductModel.Product;
import lt.viko.eif.pvaiciulis.repository.CartItemRepository;
import lt.viko.eif.pvaiciulis.repository.CartRepository;
import lt.viko.eif.pvaiciulis.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public List<CartItemResponse> getCartItemsForUser(Integer userId) {
        List<CartItem> cartItemList = cartItemRepository.findAllByCartUserId(userId);
        List<CartItemResponse> cartItemResponseList = new ArrayList<>();

        for(CartItem cartItem : cartItemList) {
            cartItemResponseList.add(CartItemResponse.builder()
                    .cartId(cartItem.getId())
                    .productId(cartItem.getProduct().getId())
                    .quantity(cartItem.getQuantity())
                    .build()
            );
        }

        return cartItemResponseList;
    }
    public List<CartProductResponse> getCartProductsForUser(Integer userId) {
        List<CartItem> cartItemList = cartItemRepository.findAllByCartUserId(userId);

        List<CartProductResponse> cartProductResponses = new ArrayList<>();

        for (CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();

            ProductResponse productResponse = ProductResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .category(product.getCategory().toString())
                    .imageUrl(product.getImageUrl())
                    .success(true)
                    .build();

            CartItemResponse cartItemResponse = CartItemResponse.builder()
                    .cartId(cartItem.getId())
                    .productId(productResponse.getId())
                    .quantity(cartItem.getQuantity())
                    .build();

            CartProductResponse res = CartProductResponse.builder()
                    .product(productResponse)
                    .cartItem(cartItemResponse)
                    .build();
            cartProductResponses.add(res);
        }

        return cartProductResponses;
    }


    public CartItemResponse createCartItem(Integer userId, CartItemRequest request) {
        try {
            if(request.getProductId() == null || request.getProductId().toString().isBlank()){
                return CartItemResponse.builder()
                        .success(false)
                        .error("Product id not provided")
                        .build();
            }
            if(request.getQuantity() == null || request.getQuantity().toString().isBlank()){
                return CartItemResponse.builder()
                        .success(false)
                        .error("Quantity not provided")
                        .build();
            }

            // Fetch Cart from the database
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

            // Fetch Product from the database
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            CartItem cartItem = cartItemRepository.findByCartUserIdAndProductId(userId, request.getProductId()).orElse(null);

            System.out.println(cartItem == null);
            if(cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());

                System.out.println(cartItem.getQuantity() + request.getQuantity());

                cartItemRepository.save(cartItem);

                return CartItemResponse.builder()
                        .cartId(cart.getId())
                        .productId(product.getId())
                        .quantity(cartItem.getQuantity())
                        .success(true)
                        .build();
            }

            // Create the CartItem
            CartItem NewcartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();

            // Save the CartItem in the repository
            cartItemRepository.save(NewcartItem);

            // Build and return the response
            return CartItemResponse.builder()
                    .cartId(cart.getId())
                    .productId(product.getId())
                    .quantity(NewcartItem.getQuantity())
                    .success(true)
                    .build();

        } catch (ResourceNotFoundException e) {
            return CartItemResponse.builder()
                    .success(false)
                    .error(e.getMessage())
                    .build();
        } catch (Exception e) {
            return CartItemResponse.builder()
                    .success(false)
                    .error("An unexpected error occurred.")
                    .build();
        }
    }


    public CartItemResponse updateCartItemForUser(Integer userId, CartItemRequest request) {
        try {

            // Fetch the CartItem by ID and associated User ID
            CartItem cartItem = cartItemRepository.findByCartUserIdAndProductId(userId, request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Cart item not found"));


            // Update the CartItem
            if(!request.getQuantity().toString().isBlank()){
                cartItem.setQuantity(request.getQuantity());
            }

            if(!request.getProductId().toString().isBlank()){
                // Validate and fetch the Product from the database
                Product product = productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                cartItem.setProduct(product);
            }

            // Save the updated CartItem in the repository
            CartItem updatedCartItem = cartItemRepository.save(cartItem);

            // Build and return the success response
            return CartItemResponse.builder()
                    .cartId(updatedCartItem.getCart().getId())
                    .productId(updatedCartItem.getProduct().getId())
                    .quantity(updatedCartItem.getQuantity())
                    .success(true)
                    .build();

        } catch (RuntimeException e) {
            // Build and return the error response
            return CartItemResponse.builder()
                    .success(false)
                    .error(e.getMessage())
                    .build();
        } catch (Exception e) {
            // Handle unexpected errors
            return CartItemResponse.builder()
                    .success(false)
                    .error("An unexpected error occurred.")
                    .build();
        }
    }

    public void deleteCartItemForUser(Integer userId, Integer productId) {
        var cartItem = cartItemRepository.findByCartUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItemRepository.delete(cartItem);
    }
}

