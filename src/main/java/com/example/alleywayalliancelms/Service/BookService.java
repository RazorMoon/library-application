package com.example.alleywayalliancelms.Service;

import com.example.alleywayalliancelms.Exception.BookNotFoundException;
import com.example.alleywayalliancelms.Model.Author;
import com.example.alleywayalliancelms.Model.Book;
import com.example.alleywayalliancelms.Model.Category;
import com.example.alleywayalliancelms.Model.Genre;
import com.example.alleywayalliancelms.Repository.BookRepository;
import com.example.alleywayalliancelms.Repository.CategoryRepository;
import com.example.alleywayalliancelms.Repository.GenreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Slf4j
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorService authorService;

    public List<Book> getListOfAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getListOfBooksAfterAgeEighteen() {
        return bookRepository.getBookByAllowedAgeAfter(18);
    }

    public List<Book> getBookByTitle(String bookTitle) {
        return bookRepository.getBookByTitle(bookTitle);
    }

    public Book getOneBookByTitle(String bookTitle) {
        return bookRepository.getOneBookByTitle(bookTitle);
    }

    public void saveExistingAuthorsToExistingBook(Long bookId, Long authorId) {
        try {
            Author authorDb = authorService.getAuthorById(authorId).get();
            Book bookDb = bookRepository.findById(bookId).get();

            Set<Author> authors = new HashSet<>();

            authors.addAll(bookDb.getAuthors());
            authors.add(authorDb);
            bookDb.setAuthors(authors);

            bookRepository.save(bookDb);
        } catch (IllegalArgumentException e) {
            log.info("Error:" + e);
        }

    }

    public void saveNewAuthorToExistingBook(Long bookId, Author author) {
        try {
            Book bookDb = bookRepository.findById(bookId).get();
            Set<Author> authors = new HashSet<>();

            authors.addAll(bookDb.getAuthors());
            authors.add(author);
            bookDb.setAuthors(authors);

            bookRepository.save(bookDb);
        } catch (IllegalArgumentException e) {
            log.info("Error:" + e);
        }

    }

    public boolean deleteBook(Long bookId) {
        if (bookId != null) {
            Book bookDb = bookRepository.findById(bookId).get();
            bookDb.setAuthors(null);
            bookDb.setBookCopies(null);
            bookDb.setWaitlistpatronAccounts(null);
            save(bookDb);
            bookRepository.delete(bookDb);
            return true;
        } else return false;
    }

    public Book getBookById(Long id) throws BookNotFoundException {
        try {
            return bookRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new BookNotFoundException("Book id is not found!" + id, e);
        }
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


    public List<Book> getBooksByCategory(Long id) {
        return bookRepository.findBooksByCategory(id);
    }

    public List<Book> getBooksByCategoryName(String name) {
        return bookRepository.findBooksByCategories(categoryRepository.findCategoryByCategoryName(name));
    }

    public Long getBookCount() {
        return bookRepository.count();
    }

    public List<Book> getBooksByAuthorName(String name) {
        return bookRepository.getBookByAuthor(name);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }


    public void saveWithInternalAuthor(Book book, Long authorId) {
        Author authorDb = authorService.getAuthorById(authorId).get();
        Set<Author> authorSet = new HashSet<>();
        authorSet.add(authorDb);
        book.setAuthors(authorSet);
        try {
            bookRepository.save(book);
        } catch(IllegalArgumentException ex) {
            log.info("Exception in " + ex);
        }
    }

    public void saveWithAuthor(Book book, Author author) {
        Set<Author> authorSet = new HashSet<>();
        authorSet.add(author);
        book.setAuthors(authorSet);
        try {
            bookRepository.save(book);
        } catch (IllegalArgumentException ex) {
            log.info("Exception in " + ex);
        }
    }

    public void saveCategory(Category category) {
        if (category != null) {
            categoryRepository.save(category);
        }
    }

    public void addGenre(Genre genre) {
        if (genre != null) {
            genreRepository.save(genre);
        }
    }
}
