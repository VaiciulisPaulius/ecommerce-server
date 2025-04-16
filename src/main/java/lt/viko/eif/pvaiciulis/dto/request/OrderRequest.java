package lt.viko.eif.pvaiciulis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lt.viko.eif.pvaiciulis.model.OrderModel.OrderStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private List<OrderItemRequest> items;
    private Double totalPrice;
    private OrderStatus status;
}
