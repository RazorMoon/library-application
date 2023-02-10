package com.example.alleywayalliancelms.Service;

import com.example.alleywayalliancelms.Model.Author;
import com.example.alleywayalliancelms.Repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getListOfAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthorById(Long authorId) {
        return authorRepository.findById(authorId);
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }
}
