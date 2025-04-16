package lt.viko.eif.pvaiciulis.repository;

import lt.viko.eif.pvaiciulis.model.CartModel.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserId(Integer userId);
}
