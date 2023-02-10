package com.example.alleywayalliancelms.Repository;

import com.example.alleywayalliancelms.Model.PatronAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronAccountRepository extends JpaRepository<PatronAccount, String> {

    PatronAccount getPatronAccountByEmail(String email);


}