package unibuc.carServiceManagement.exception.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import unibuc.carServiceManagement.exception.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorControllerAdvice {
    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handle(NotFoundException exception) {
        String message = "At " + LocalDateTime.now() + ": " + exception.getMessage();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("message", message);
        modelAndView.setViewName("exception");
        return modelAndView;

    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationErrors(MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("message", message);
        modelAndView.setViewName("exception");
        return modelAndView;

    }

    @ExceptionHandler({InvalidFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationErrors(InvalidFormatException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("message", exception.getMessage());
        modelAndView.setViewName("exception");
        return modelAndView;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationErrors(MethodArgumentTypeMismatchException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("message", exception.getMessage());
        modelAndView.setViewName("exception");
        return modelAndView;
    }

    @ExceptionHandler({SpecialityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handle(SpecialityException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("message", exception.getMessage());
        modelAndView.setViewName("exception");
        return modelAndView;
    }

    @ExceptionHandler({DuplicateCarServiceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handle(DuplicateCarServiceException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("message", exception.getMessage());
        modelAndView.setViewName("exception");
        return modelAndView;
    }

    @ExceptionHandler({WrongDatesOrderException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handle(WrongDatesOrderException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("message", exception.getMessage());
        modelAndView.setViewName("exception");
        return modelAndView;
    }

    @ExceptionHandler({AppointmentsOverlappingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handle(AppointmentsOverlappingException exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("message", exception.getMessage());
        modelAndView.setViewName("exception");
        return modelAndView;
    }

    @ExceptionHandler({TaskAlreadyExistingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handle(TaskAlreadyExistingException exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("message", exception.getMessage());
        modelAndView.setViewName("exception");
        return modelAndView;
    }

}
