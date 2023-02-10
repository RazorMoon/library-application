package com.example.alleywayalliancelms.RestControllers;

import com.example.alleywayalliancelms.Exception.BookNotFoundException;
import com.example.alleywayalliancelms.Model.Book;
import com.example.alleywayalliancelms.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @GetMapping("books")
    public List<Book> bookList() {
        return bookService.getListOfAllBooks();
    }

    @GetMapping("/books/id/{id}")
    public Book bookById (@PathVariable Long id) throws BookNotFoundException {
        return bookService.getBookById(id);
    }

    @GetMapping("/books/category/name/{name}")
    public List<Book> getBooksByCatgoryName(@PathVariable String name) {
        return bookService.getBooksByCategoryName(name);
    }

    @GetMapping("/books/author/{author}")
    public List<Book> bookByAuthor(@PathVariable String author) {
        return bookService.getBooksByAuthorName(author);
    }

    @GetMapping("/books/find/category/{category}")
    public List<Book> booksByCategory(@PathVariable Long category) {
        return bookService.getBooksByCategory(category);
    }

    @PostMapping("/books/add")
    public ResponseEntity<?> addNew(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
    }
}

