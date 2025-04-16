package lt.viko.eif.pvaiciulis.model.UserModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue
    private Integer id;

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false, unique = true)
    private User user;
}
