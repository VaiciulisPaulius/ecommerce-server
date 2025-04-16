package lt.viko.eif.pvaiciulis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unable to access this resource.");
    }
}

