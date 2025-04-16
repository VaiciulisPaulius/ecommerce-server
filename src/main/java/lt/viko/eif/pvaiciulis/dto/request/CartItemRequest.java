package lt.viko.eif.pvaiciulis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {
    private Integer productId;
    private Integer quantity;
}

//@Id
//@GeneratedValue
//private Integer id;
//
//@ManyToOne
//@JoinColumn(name = "cartId", nullable = false)
//private Cart cart;
//
//@ManyToOne
//@JoinColumn(name = "productId", nullable = false)
//private Product product;
//
//private Integer quantity;
