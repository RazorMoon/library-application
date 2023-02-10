package com.example.alleywayalliancelms.RestControllers;

import com.example.alleywayalliancelms.Model.Author;
import com.example.alleywayalliancelms.Service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class AuthorRestController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("authors")
    public List<Author> authorList(){
        return authorService.getListOfAllAuthors();
    }

    @PostMapping("authors/add")
    public ResponseEntity<?> addNew(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.save(author), HttpStatus.CREATED);
    }
}
