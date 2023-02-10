package com.example.alleywayalliancelms.Repository;

import com.example.alleywayalliancelms.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}