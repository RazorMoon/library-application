package com.example.alleywayalliancelms.repository;

import com.example.alleywayalliancelms.model.PatronAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronAccountRepository extends JpaRepository<PatronAccount, String> {

    PatronAccount getPatronAccountByEmail(String email);

}