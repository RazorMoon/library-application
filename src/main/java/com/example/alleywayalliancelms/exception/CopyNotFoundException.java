package com.example.alleywayalliancelms.exception;

public class CopyNotFoundException extends BaseException{

    public CopyNotFoundException() {
        super("Given copy id is not found, or not exist.");
    }

    public CopyNotFoundException(String message) {
        super(message);
    }

    public CopyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
