package com.example.alleywayalliancelms.repository;

import com.example.alleywayalliancelms.model.Book;
import com.example.alleywayalliancelms.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {




}