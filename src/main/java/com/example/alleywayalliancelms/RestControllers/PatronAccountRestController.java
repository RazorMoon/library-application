package com.example.alleywayalliancelms.RestControllers;

import com.example.alleywayalliancelms.Model.PatronAccount;
import com.example.alleywayalliancelms.Repository.PatronAccountRepository;
import com.example.alleywayalliancelms.Service.PatronAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class PatronAccountRestController {

    @Autowired
    private PatronAccountService patronAccountService;

    @GetMapping("accounts")
    public List<PatronAccount> getListPatronAccount() {
        return patronAccountService.getAllAccounts();
    }

    @PostMapping("accounts/add")
    public ResponseEntity<?> addNewAccount(@RequestBody PatronAccount patronAccount) {
        return new ResponseEntity<>(patronAccountService.save(patronAccount), HttpStatus.CREATED);
    }

    @GetMapping("accounts/get/email/{email}")
    public PatronAccount getAccountByEmail(@PathVariable String email) {
        return patronAccountService.getAccountByEmail(email);
    }
}
