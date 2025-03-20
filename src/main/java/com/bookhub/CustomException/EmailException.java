package com.bookhub.CustomException;

public class EmailException extends RuntimeException {
    public EmailException(String message) {
        super(message);
    }
}
