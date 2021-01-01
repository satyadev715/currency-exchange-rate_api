package com.currencyexchangeratetracker.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RequestValidationHandler {
    private static Logger log = LoggerFactory.getLogger(RequestValidationHandler.class);
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        log.debug("handleAllExceptions : "+ex.fillInStackTrace());

        ErrorResponse responseMessage = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CurrencyNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleCurrencyException(CurrencyNotSupportedException ex, WebRequest request) {
        log.debug("handleCurrencyException : "+ex.fillInStackTrace());
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestDateException.class)
    public ResponseEntity<ErrorResponse> handleRequestDateException(RequestDateException ex, WebRequest request) {
        log.debug("handleRequestDateException : "+ex.fillInStackTrace());
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Validation Failed",
                ex.getBindingResult().toString());

        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
