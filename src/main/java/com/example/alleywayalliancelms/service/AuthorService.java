package com.example.alleywayalliancelms.service;

import com.example.alleywayalliancelms.exception.AuthorNotFoundException;
import com.example.alleywayalliancelms.model.Author;
import com.example.alleywayalliancelms.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<Author> getListOfAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(Long authorId) {
        Optional<Author> byId = authorRepository.findById(authorId);

        if (byId.isPresent()) {
            return byId.get();
        } else {
            log.error("Author with given id : {} is not found", authorId);
            throw new AuthorNotFoundException();
        }
    }

}
