package com.example.alleywayalliancelms.exception;

public class AuthorNotFoundException extends BaseException {

    public AuthorNotFoundException() {
        super("Author with given id, is not exist or not valid.");
    }
}
