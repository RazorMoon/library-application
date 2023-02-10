package com.example.alleywayalliancelms.Repository;

import com.example.alleywayalliancelms.Model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    @Query(value = "SELECT max(publisher_id) FROM publisher", nativeQuery = true)
    public Long getMaxIdVal();
}