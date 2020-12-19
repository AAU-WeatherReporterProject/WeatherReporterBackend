package at.aau.projects.weatherreporter.rest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<Object> handleServiceValidationException(HttpClientErrorException ex) {
        HttpStatus status = ex.getStatusCode() != null ? ex.getStatusCode() : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(ex.getLocalizedMessage());
    }
}
