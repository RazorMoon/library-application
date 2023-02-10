package com.example.alleywayalliancelms.Repository;

import com.example.alleywayalliancelms.Model.Book;
import com.example.alleywayalliancelms.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> getBookByAllowedAgeAfter(Integer age);

    @Query(value = "select * from book where lower(title) like %:keyword%", nativeQuery = true)
    List<Book> getBookByTitle(@Param("keyword") String keyword);

    @Query(value = "select * from book where lower(title) like %:keyword%", nativeQuery = true)
    Book getOneBookByTitle(@Param("keyword") String keyword);

    @Query(value = "select book.book_id, book.title, book.genre, book.allowed_age, book.category from book INNER JOIN book_author ON book.book_id = book_author.book_id LEFT JOIN author ON book_author.author_id = author.author_id WHERE lower(author.name) like %:author%", nativeQuery = true)
    List<Book> getBookByAuthor(@Param("author") String author);

    List<Book> findBooksByCategory(Long id);

    List<Book> findBooksByCategories(Category category);


}