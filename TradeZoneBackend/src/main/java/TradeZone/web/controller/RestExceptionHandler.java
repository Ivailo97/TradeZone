package TradeZone.web.controller;

import TradeZone.data.error.ApiError;
import TradeZone.data.error.exception.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponseEntity(ex);
    }

    @ExceptionHandler(NotAllowedException.class)
    protected ResponseEntity<Object> handleNotAllowed(NotAllowedException ex) {
        return buildResponseEntity(ex);
    }

    @ExceptionHandler(AdvertisementNotValidException.class)
    protected ResponseEntity<Object> handleAdvertisementNotValid(AdvertisementNotValidException ex) {
        return buildResponseEntity(ex);
    }

    @ExceptionHandler(SearchNotValidException.class)
    protected ResponseEntity<Object> handleSearchNotValid(SearchNotValidException ex) {
        return buildResponseEntity(ex);
    }

    @ExceptionHandler(DeleteRequestNotValidException.class)
    protected ResponseEntity<Object> handleDeleteRequestNotValid(DeleteRequestNotValidException ex) {
        return buildResponseEntity(ex);
    }

    @ExceptionHandler(ProfileNotCompletedException.class)
    protected ResponseEntity<Object> handleProfileNotCompleted(ProfileNotCompletedException ex) {
        return buildResponseEntity(ex);
    }

    @ExceptionHandler(ViewsUpdateNotValidException.class)
    protected ResponseEntity<Object> handleViewsUpdateNotValid(ViewsUpdateNotValidException ex) {
        return buildResponseEntity(ex);
    }

    @ExceptionHandler(MessageToSendNotValidException.class)
    protected ResponseEntity<Object> handleMessageNotValid(MessageToSendNotValidException ex) {
        return buildResponseEntity(ex);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private ResponseEntity<Object> buildResponseEntity(Throwable throwable) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(throwable.getMessage());
        apiError.setDebugMessage(throwable.getLocalizedMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
