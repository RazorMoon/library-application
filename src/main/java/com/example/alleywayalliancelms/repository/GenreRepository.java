package com.example.alleywayalliancelms.repository;

import com.example.alleywayalliancelms.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}