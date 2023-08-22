package com.example.alleywayalliancelms.repository;

import com.example.alleywayalliancelms.model.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {


    List<BookCopy> findBookCopiesByBookId(Long bookId);

}
