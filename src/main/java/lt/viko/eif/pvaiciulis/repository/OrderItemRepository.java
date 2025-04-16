package lt.viko.eif.pvaiciulis.repository;

import lt.viko.eif.pvaiciulis.model.OrderModel.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findAllByOrderUserId(Integer userId);
    Optional<OrderItem> findByOrderIdAndProductId(Integer orderId, Integer productId);
}
