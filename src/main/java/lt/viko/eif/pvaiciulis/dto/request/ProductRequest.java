package lt.viko.eif.pvaiciulis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String category; // You may need to map the category to an Enum in the service layer
}