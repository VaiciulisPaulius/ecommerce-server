package lt.viko.eif.pvaiciulis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    private Integer id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    private boolean success;
    private String error;
}

