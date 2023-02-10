package com.example.alleywayalliancelms.Repository;

import com.example.alleywayalliancelms.Model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}