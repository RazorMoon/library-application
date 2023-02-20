package com.example.alleywayalliancelms.repository;

import com.example.alleywayalliancelms.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}