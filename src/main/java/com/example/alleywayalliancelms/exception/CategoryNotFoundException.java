package com.example.alleywayalliancelms.exception;

public class CategoryNotFoundException extends BaseException {

    public CategoryNotFoundException() {
        super("Given category is not found or not exist.");
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
