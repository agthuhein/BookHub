package com.bookhub.CustomException;

public class BookOutOfStockException extends RuntimeException {
    public BookOutOfStockException(String message) {
        super(message);
    }
}
