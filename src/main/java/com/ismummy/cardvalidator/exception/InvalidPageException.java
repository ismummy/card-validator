package com.ismummy.cardvalidator.exception;

public class InvalidPageException extends RuntimeException {
    public InvalidPageException(){}

    public InvalidPageException(String message){
        super(message);
    }
}
