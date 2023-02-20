package com.example.alleywayalliancelms.exception;

public class BaseException extends RuntimeException {

    public BaseException() {
        super("Given id is not exist.");
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
