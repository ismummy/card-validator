package com.ismummy.cardvalidator.exception;

import com.ismummy.cardvalidator.helpers.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {InvalidInputException.class})
    public ResponseEntity<?> invalidInputException(InvalidInputException ex){
        logger.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidPageException.class})
    public ResponseEntity<?> invalidPageException(InvalidPageException ex){
        logger.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpStatusCodeException.class})
    public ResponseEntity<?> statusCodeException(HttpStatusCodeException ex){
        logger.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex) {
        logger.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
