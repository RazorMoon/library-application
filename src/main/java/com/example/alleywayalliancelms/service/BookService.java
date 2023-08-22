package com.example.alleywayalliancelms.service;

import com.example.alleywayalliancelms.exception.BookNotFoundException;
import com.example.alleywayalliancelms.exception.CategoryNotFoundException;
import com.example.alleywayalliancelms.exception.GenreNotFoundException;
import com.example.alleywayalliancelms.model.Author;
import com.example.alleywayalliancelms.model.Book;
import com.example.alleywayalliancelms.model.Category;
import com.example.alleywayalliancelms.model.Genre;
import com.example.alleywayalliancelms.repository.BookRepository;
import com.example.alleywayalliancelms.repository.CategoryRepository;
import com.example.alleywayalliancelms.repository.GenreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class BookService {
    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;

    private final GenreRepository genreRepository;

    private final AuthorService authorService;

    @Autowired
    public BookService(BookRepository bookRepository,
                       CategoryRepository categoryRepository,
                       GenreRepository genreRepository,
                       AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.genreRepository = genreRepository;
        this.authorService = authorService;
    }

    private Book checkIfBookIsPresent(Optional<Book> book) {
        if (book.isPresent()) {
            return book.get();
        } else {
            throw new BookNotFoundException();
        }
    }

    public List<Book> getListOfAllBooks() {
        return bookRepository.findAll();
    }

    public void saveExistingAuthorsToExistingBook(Long bookId, Long authorId) {
        Author authorDb = authorService.getAuthorById(authorId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Book bookDb = checkIfBookIsPresent(bookOptional);

        Set<Author> authors = new HashSet<>(bookDb.getAuthors());
        authors.add(authorDb);
        bookDb.setAuthors(authors);

        bookRepository.save(bookDb);
    }


    public void saveNewAuthorToExistingBook(Long bookId, Author author) {
        Optional<Book> bookDb = bookRepository.findById(bookId);
        Book book = checkIfBookIsPresent(bookDb);

        Set<Author> authors = new HashSet<>(book.getAuthors());

        authors.add(author);
        book.setAuthors(authors);

        bookRepository.save(book);
    }

    public boolean deleteBook(Long bookId) {
        if (bookId != null) {
            Optional<Book> bookOpt = bookRepository.findById(bookId);
            Book bookDb = checkIfBookIsPresent(bookOpt);

            bookDb.setAuthors(null);
            bookDb.setBookCopies(null);
            bookDb.setWaitlistpatronAccounts(null);

            save(bookDb);
            bookRepository.delete(bookDb);
            return true;
        } else
            return false;
    }

    public Book getBookById(Long id) {
        return checkIfBookIsPresent(bookRepository.findById(id));
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    public List<Author> getAuthors() {
        return authorService.getListOfAllAuthors();
    }

    public Long getBookCount() {
        return bookRepository.count();
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void saveWithInternalAuthor(Book book, Long authorId) {
        Author authorDb = authorService.getAuthorById(authorId);
        Set<Author> authorSet = new HashSet<>();
        authorSet.add(authorDb);
        book.setAuthors(authorSet);
        bookRepository.save(book);
    }

    public void saveWithAuthor(Book book, Author author) {
        Set<Author> authorSet = new HashSet<>();
        authorSet.add(author);
        book.setAuthors(authorSet);
        bookRepository.save(book);
    }

    public void saveCategory(Category category) {
        if (category != null) {
            categoryRepository.save(category);
        } else {
            log.error("Given category is null.");
            throw new CategoryNotFoundException();
        }
    }

    public void addGenre(Genre genre) {
        if (genre != null) {
            genreRepository.save(genre);
        } else {
            log.error("Given genre is null.");
            throw new GenreNotFoundException();
        }
    }

}
