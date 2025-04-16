package lt.viko.eif.pvaiciulis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a response object for user registration.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private boolean success;
    private String token;
    private String error;
}