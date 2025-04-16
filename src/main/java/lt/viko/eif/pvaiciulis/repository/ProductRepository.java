package lt.viko.eif.pvaiciulis.repository;

import lt.viko.eif.pvaiciulis.model.ProductModel.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
