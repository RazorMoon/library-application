package com.example.alleywayalliancelms.repository;

import com.example.alleywayalliancelms.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

}