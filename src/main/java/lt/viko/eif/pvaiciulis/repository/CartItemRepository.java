package lt.viko.eif.pvaiciulis.repository;

import lt.viko.eif.pvaiciulis.model.CartModel.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findAllByCartUserId(Integer userId);
    Optional<CartItem> findByCartUserIdAndProductId(Integer userId, Integer productId);
}
