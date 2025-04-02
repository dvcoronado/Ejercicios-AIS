package es.codeurjc.web.nitflex.configuration;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import es.codeurjc.web.nitflex.service.exceptions.FilmNotFoundException;

@ControllerAdvice(basePackages = "es.codeurjc.web.nitflex.controller.web")
public class WebErrorHandler {

    // Constante para el nombre del atributo "message"
    private static final String MESSAGE_ATTRIBUTE = "message";

    /**
     * When a 'FilmNotFound' exception occurs, the following method is executed
     * @param ex
     * @return a view with a message indicating the error
     */
    @ExceptionHandler({FilmNotFoundException.class, IllegalArgumentException.class, BindException.class})
    public ModelAndView handleException(Exception ex){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(MESSAGE_ATTRIBUTE);
        modelAndView.addObject("error", true);

        if(ex instanceof MethodArgumentNotValidException manvExp){
            // Comprobamos si getFieldError() es null antes de acceder a getDefaultMessage()
            if (manvExp.getFieldError() == null) {
                throw new NullPointerException("Field error is null");
            }
            else{
                modelAndView.addObject(MESSAGE_ATTRIBUTE, manvExp.getFieldError().getDefaultMessage());
            }
        } else {
            modelAndView.addObject(MESSAGE_ATTRIBUTE, ex.getMessage());
        }

        return modelAndView;
    }
}

