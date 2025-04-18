package lt.viko.eif.pvaiciulis.model.ProductModel;

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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String description;

    @Column(nullable = false)
    private Double price;

    private Integer stock;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String imageUrl;
}