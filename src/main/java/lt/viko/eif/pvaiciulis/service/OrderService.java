package lt.viko.eif.pvaiciulis.service;

import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.dto.request.OrderItemRequest;
import lt.viko.eif.pvaiciulis.dto.request.OrderRequest;
import lt.viko.eif.pvaiciulis.dto.response.OrderItemResponse;
import lt.viko.eif.pvaiciulis.dto.response.OrderResponse;
import lt.viko.eif.pvaiciulis.exception.ResourceNotFoundException;
import lt.viko.eif.pvaiciulis.model.CartModel.Cart;
import lt.viko.eif.pvaiciulis.model.CartModel.CartItem;
import lt.viko.eif.pvaiciulis.model.OrderModel.Order;
import lt.viko.eif.pvaiciulis.model.OrderModel.OrderItem;
import lt.viko.eif.pvaiciulis.model.OrderModel.OrderStatus;
import lt.viko.eif.pvaiciulis.model.ProductModel.Product;
import lt.viko.eif.pvaiciulis.model.UserModel.User;
import lt.viko.eif.pvaiciulis.repository.CartRepository;
import lt.viko.eif.pvaiciulis.repository.OrderRepository;
import lt.viko.eif.pvaiciulis.repository.ProductRepository;
import lt.viko.eif.pvaiciulis.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public List<OrderResponse> getOrdersForUser(Integer userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream()
                .map(order -> OrderResponse.builder()
                        .orderId(order.getId())
                        .totalPrice(order.getTotalPrice())
                        .status(order.getStatus())
                        .createdAt(order.getCreatedAt())
                        .items(order.getItems().stream().map(item -> OrderItemResponse.builder()
                                .productId(item.getProduct().getId())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .build()).toList())
                        .success(true)
                        .build())
                .toList();
    }

    public OrderResponse createOrder(Integer userId, OrderRequest request) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<OrderItem> items = new ArrayList<>();
            for (OrderItemRequest itemRequest : request.getItems()) {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                items.add(OrderItem.builder()
                        .product(product)
                        .quantity(itemRequest.getQuantity())
                        .price(itemRequest.getPrice())
                        .build());
            }

            Order order = Order.builder()
                    .user(user)
                    .items(items)
                    .totalPrice(request.getTotalPrice())
                    .status(OrderStatus.valueOf(request.getStatus().name()))
                    .createdAt(new Date())
                    .build();

            order.getItems().forEach(item -> item.setOrder(order));
            Order savedOrder = orderRepository.save(order);

            return OrderResponse.builder()
                    .orderId(savedOrder.getId())
                    .totalPrice(savedOrder.getTotalPrice())
                    .status(savedOrder.getStatus())
                    .createdAt(savedOrder.getCreatedAt())
                    .items(savedOrder.getItems().stream().map(item -> OrderItemResponse.builder()
                            .productId(item.getProduct().getId())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build()).toList())
                    .success(true)
                    .build();

        } catch (RuntimeException e) {
            return OrderResponse.builder()
                    .success(false)
                    .error(e.getMessage())
                    .build();
        }
    }

    public OrderResponse createOrderFromCart(Integer userId) {
        try {
            System.out.println("1");
            // Fetch the user and cart
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

            System.out.println("2");

            // Create order items from the cart items
            List<OrderItem> orderItems = new ArrayList<>();
            double totalPrice = 0.0;

            for (CartItem cartItem : cart.getItems()) {
                Product product = cartItem.getProduct();
                double itemPrice = product.getPrice() * cartItem.getQuantity();
                totalPrice += itemPrice;

                orderItems.add(OrderItem.builder()
                        .product(product)
                        .quantity(cartItem.getQuantity())
                        .price(itemPrice)
                        .build());
            }

            System.out.println("3");

            // Create and save the order
            Order order = Order.builder()
                    .user(user)
                    .items(orderItems)
                    .totalPrice(totalPrice)
                    .status(OrderStatus.PENDING) // Default status
                    .createdAt(new Date())
                    .build();

            System.out.println("4");

            order.getItems().forEach(item -> item.setOrder(order));
            Order savedOrder = orderRepository.save(order);

            System.out.println("5");

            // Clear the cart after creating the order (optional)
//            cart.getItems().clear();
//            cartRepository.save(cart);

            System.out.println("6");

            // Build the response
            return OrderResponse.builder()
                    .orderId(savedOrder.getId())
                    .totalPrice(savedOrder.getTotalPrice())
                    .status(savedOrder.getStatus())
                    .createdAt(savedOrder.getCreatedAt())
                    .items(savedOrder.getItems().stream().map(item -> OrderItemResponse.builder()
                            .productId(item.getProduct().getId())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build()).toList())
                    .success(true)
                    .build();

        } catch (ResourceNotFoundException e) {
            return OrderResponse.builder()
                    .success(false)
                    .error(e.getMessage())
                    .build();
        } catch (Exception e) {
            return OrderResponse.builder()
                    .success(false)
                    .error("An unexpected error occurred.")
                    .build();
        }
    }

    public OrderResponse updateOrder(Integer userId, Integer orderId, OrderRequest request) {
        try {
            // Fetch the order
            Order order = orderRepository.findByIdAndUserId(orderId, userId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Update status if provided
            if (request.getStatus() != null) {
                order.setStatus(OrderStatus.valueOf(request.getStatus().name()));
            }

            // Update total price if provided
            if (request.getTotalPrice() != null) {
                order.setTotalPrice(request.getTotalPrice());
            }

            // Update order items if provided
            if (request.getItems() != null && !request.getItems().isEmpty()) {
                List<OrderItem> updatedItems = new ArrayList<>();

                for (OrderItemRequest itemRequest : request.getItems()) {
                    Product product = productRepository.findById(itemRequest.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    // Find existing order item and update it
                    OrderItem existingItem = order.getItems().stream()
                            .filter(item -> item.getProduct().getId().equals(itemRequest.getProductId()))
                            .findFirst()
                            .orElse(null);

                    if (existingItem != null) {
                        // If the item exists, update its quantity and price
                        if (itemRequest.getQuantity() != null) {
                            existingItem.setQuantity(itemRequest.getQuantity());
                        }
                        if (itemRequest.getPrice() != null) {
                            existingItem.setPrice(itemRequest.getPrice());
                        }
                        updatedItems.add(existingItem);
                    } else {
                        // If the item does not exist, create a new one
                        updatedItems.add(OrderItem.builder()
                                .product(product)
                                .quantity(itemRequest.getQuantity())
                                .price(itemRequest.getPrice())
                                .build());
                    }
                }

                // Clear the current items and add the updated items
                order.getItems().clear();
                updatedItems.forEach(item -> item.setOrder(order));
                order.getItems().addAll(updatedItems);
            }

            // Recalculate the total price
            double newTotalPrice = order.getItems().stream()
                    .mapToDouble(item -> item.getQuantity() * item.getPrice())
                    .sum();
            order.setTotalPrice(newTotalPrice);

            // Save the updated order
            Order savedOrder = orderRepository.save(order);

            // Return the response
            return OrderResponse.builder()
                    .orderId(savedOrder.getId())
                    .totalPrice(savedOrder.getTotalPrice())
                    .status(savedOrder.getStatus())
                    .createdAt(savedOrder.getCreatedAt())
                    .items(savedOrder.getItems().stream().map(item -> OrderItemResponse.builder()
                            .productId(item.getProduct().getId())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build()).toList())
                    .success(true)
                    .build();

        } catch (RuntimeException e) {
            return OrderResponse.builder()
                    .success(false)
                    .error(e.getMessage())
                    .build();
        }
    }


    public void deleteOrder(Integer userId, Integer orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }
}


