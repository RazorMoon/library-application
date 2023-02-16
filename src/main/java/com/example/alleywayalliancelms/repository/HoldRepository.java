package com.example.alleywayalliancelms.repository;

import com.example.alleywayalliancelms.model.Hold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoldRepository extends JpaRepository<Hold, Long> {

    @Query(value = "SELECT * FROM hold WHERE end_time=(SELECT MAX(end_time) FROM hold WHERE book_copy_id = :copyId)", nativeQuery = true)
    Hold findFirstByEndTimeAndBookCopyIdDesc(@Param("copyId") Long copyId);

    @Query(value = "SELECT MAX(hold_id) FROM hold", nativeQuery = true)
    Long getLastId();

    List<Hold> findHoldsByPatronId(String accountId);
}