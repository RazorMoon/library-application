package com.example.alleywayalliancelms.exceptionHandler;

import com.example.alleywayalliancelms.exception.AuthorNotFoundException;
import com.example.alleywayalliancelms.exception.BookNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BookNotFoundException.class, AuthorNotFoundException.class})
    protected ResponseEntity<ExceptionResponse> handException(Exception ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, NullPointerException.class})
    protected ResponseEntity<ExceptionResponse> argumentException(Exception ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
