package com.example.alleywayalliancelms.exception;

public class CheckoutNotFoundException extends BaseException {

    public CheckoutNotFoundException() {
        super("Given checkout is not found.");
    }

    public CheckoutNotFoundException(String message) {
        super(message);
    }

    public CheckoutNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
