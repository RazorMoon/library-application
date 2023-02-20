package com.example.alleywayalliancelms.exception;

public class GenreNotFoundException extends BaseException {
    public GenreNotFoundException() {
        super("Given genre is not found or not exist.");
    }

    public GenreNotFoundException(String message) {
        super(message);
    }

    public GenreNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
