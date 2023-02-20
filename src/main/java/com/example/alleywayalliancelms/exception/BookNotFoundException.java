package com.example.alleywayalliancelms.exception;


public class BookNotFoundException extends BaseException {

    public BookNotFoundException() {
        super("Given book id is not found, or not exist.");
    }

}


