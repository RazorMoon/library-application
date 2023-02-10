package com.example.alleywayalliancelms.RestControllers;

import com.example.alleywayalliancelms.Model.BookCopy;
import com.example.alleywayalliancelms.Service.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class BookCopyRestController {

    @Autowired
    private BookCopyService bookCopyService;

    @GetMapping("copies")
    public List<BookCopy> bookCopyList() {
        return bookCopyService.getBookCopyList();
    }

    @PostMapping("copies/add")
    public ResponseEntity<?> addNewCopy(@RequestBody BookCopy bookCopy) {
        return new ResponseEntity<>(bookCopyService.save(bookCopy), HttpStatus.CREATED);
    }
}
