package lt.viko.eif.pvaiciulis.repository;

import lt.viko.eif.pvaiciulis.model.UserModel.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findByUserId(Integer userId);
}
