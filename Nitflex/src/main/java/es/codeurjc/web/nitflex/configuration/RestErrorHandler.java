package es.codeurjc.web.nitflex.configuration;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import es.codeurjc.web.nitflex.service.exceptions.FilmNotFoundException;

@ControllerAdvice(basePackages = "es.codeurjc.web.nitflex.controller.rest")
public class RestErrorHandler {

    /**
     * When a 'FilmNotFound' exception occurs, the following method is executed
     * 
     * @param ex
     * @return a view with a message indicating the error
     */
    @ExceptionHandler({ FilmNotFoundException.class, IllegalArgumentException.class, BindException.class })
    public ResponseEntity<String> handleException(Exception ex) {
        switch (ex) {
            case MethodArgumentNotValidException manvExp -> {
                if (manvExp.getFieldError() == null) {
                    throw new NullPointerException("getFieldError() es null");
                }
                else{
                    return ResponseEntity.badRequest().body( manvExp.getFieldError().getDefaultMessage() );
                }
            }
            case FilmNotFoundException fnfExp -> {
                return ResponseEntity.notFound().build();
            }
            default -> {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
        }
    }
}

