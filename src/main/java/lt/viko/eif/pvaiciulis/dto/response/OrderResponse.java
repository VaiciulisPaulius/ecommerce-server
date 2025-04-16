package lt.viko.eif.pvaiciulis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lt.viko.eif.pvaiciulis.model.OrderModel.OrderStatus;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Integer orderId;
    private List<OrderItemResponse> items;
    private Double totalPrice;
    private OrderStatus status;
    private Date createdAt;

    private boolean success;
    private String error;
}
