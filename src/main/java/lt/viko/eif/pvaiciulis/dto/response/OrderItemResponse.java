package lt.viko.eif.pvaiciulis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Double price;

    private boolean success;
    private String error;
}
