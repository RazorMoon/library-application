package com.example.alleywayalliancelms.Repository;

import com.example.alleywayalliancelms.Model.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    @Query(value = "SELECT count(*) FROM book_copy WHERE book_id = %:bookId%", nativeQuery = true)
    Long getBookCopiesCount(@Param("bookId") Long bookId);

    List<BookCopy> findBookCopiesByBookId(Long bookId);

}