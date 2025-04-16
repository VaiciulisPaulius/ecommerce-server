package lt.viko.eif.pvaiciulis.service;

import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.dto.request.OrderItemRequest;
import lt.viko.eif.pvaiciulis.dto.response.OrderItemResponse;
import lt.viko.eif.pvaiciulis.model.OrderModel.Order;
import lt.viko.eif.pvaiciulis.model.OrderModel.OrderItem;
import lt.viko.eif.pvaiciulis.model.ProductModel.Product;
import lt.viko.eif.pvaiciulis.repository.OrderItemRepository;
import lt.viko.eif.pvaiciulis.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public List<OrderItemResponse> getOrderItemsForUser(Integer userId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderUserId(userId);
        return orderItems.stream().map(orderItem -> OrderItemResponse.builder()
                .orderId(orderItem.getOrder().getId())
                .productId(orderItem.getProduct().getId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .success(true)
                .build()).collect(Collectors.toList());
    }

    public OrderItemResponse createOrderItem(OrderItemRequest request, Order order) {
        try {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(request.getPrice())
                    .build();

            OrderItem savedOrderItem = orderItemRepository.save(orderItem);

            return OrderItemResponse.builder()
                    .orderId(savedOrderItem.getOrder().getId())
                    .productId(savedOrderItem.getProduct().getId())
                    .quantity(savedOrderItem.getQuantity())
                    .price(savedOrderItem.getPrice())
                    .success(true)
                    .build();
        } catch (Exception e) {
            return OrderItemResponse.builder()
                    .success(false)
                    .error("Error creating order item: " + e.getMessage())
                    .build();
        }
    }
}

