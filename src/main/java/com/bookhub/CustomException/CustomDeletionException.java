package com.bookhub.CustomException;

public class CustomDeletionException extends RuntimeException {
    public CustomDeletionException(String message) {
        super(message);
    }
}
