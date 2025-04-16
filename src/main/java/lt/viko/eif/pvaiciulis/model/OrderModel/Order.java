package lt.viko.eif.pvaiciulis.model.OrderModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lt.viko.eif.pvaiciulis.model.UserModel.User;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
