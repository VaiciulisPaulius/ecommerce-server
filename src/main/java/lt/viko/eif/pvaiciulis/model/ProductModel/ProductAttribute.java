package lt.viko.eif.pvaiciulis.model.ProductModel;

import jakarta.persistence.*;

@Entity
@Table(name = "product_attributes")
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductAttributeKey key;

    @Column(nullable = false)
    private String value;

    // Constructors, Getters, and Setters
}

enum ProductAttributeKey {
    MANUFACTURER,
    GENERATION,
    SOCKET_TYPE,
    SCREEN_SIZE,
    RESOLUTION,
    STORAGE_TYPE,
    RAM_SIZE
}

