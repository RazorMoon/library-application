package com.example.alleywayalliancelms.service;

import com.example.alleywayalliancelms.exception.BookNotFoundException;
import com.example.alleywayalliancelms.model.Book;
import com.example.alleywayalliancelms.model.PatronAccount;
import com.example.alleywayalliancelms.model.Role;
import com.example.alleywayalliancelms.repository.PatronAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatronAccountService implements UserDetailsService {

    private final PatronAccountRepository patronAccountRepository;

    private final BookService bookService;

    private final CheckoutService checkoutService;

    private  final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PatronAccount patronAccount = patronAccountRepository.getPatronAccountByEmail(username);

        if (patronAccount == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new User(patronAccount.getEmail(),
                patronAccount.getPassword(),
                patronAccount.isEnabled(),
                true,
                true,
                true,
                patronAccount.getAuthorities());
    }

    public List<PatronAccount> getAllAccountsWithSortedCheckouts() {
        List<PatronAccount> allAccounts = patronAccountRepository.findAll();

        for (PatronAccount el : allAccounts) {
            el.setCheckouts(checkoutService.checkActiveCheckoutsForUser(el, false));
        }
        return allAccounts;
    }

    public PatronAccount updateAccountInformation(String email, PatronAccount patronAccountForm) {
        PatronAccount patronAccount = patronAccountRepository.getPatronAccountByEmail(email);
        patronAccount.setFirstName(patronAccountForm.getFirstName());
        patronAccount.setEmail(patronAccountForm.getEmail());
        patronAccount.setSurename(patronAccountForm.getSurename());
        return patronAccountRepository.save(patronAccount);
    }

    public PatronAccount getAccountByEmail(String email) {
        return patronAccountRepository.getPatronAccountByEmail(email);
    }

    public PatronAccount findPatronAccountById(String id) {
        Optional<PatronAccount> patronAccountFromDb = patronAccountRepository.findById(id);
        return patronAccountFromDb.orElse(new PatronAccount());
    }

    public void saveBookToWaitList(Long bookId, String email) throws BookNotFoundException {
        PatronAccount patronAccountFromDb = patronAccountRepository.getPatronAccountByEmail(email);
        Book book = bookService.getBookById(bookId);
        Set<Book> bookSet = new HashSet<>(patronAccountFromDb.getBooks());
        bookSet.add(book);
        patronAccountFromDb.setBooks(bookSet);

        patronAccountRepository.save(patronAccountFromDb);
    }

    public boolean checkIfBookAlreadyInWaitlist(Long bookId, String email) {
        PatronAccount patronAccountFromDb = patronAccountRepository.getPatronAccountByEmail(email);

        for (Book el : patronAccountFromDb.getBooks()) {
            if (bookId.equals(el.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean savePatronAccount(PatronAccount patronAccount) {
        PatronAccount patronAccountFromDb = patronAccountRepository.getPatronAccountByEmail(patronAccount.getEmail());

        if (patronAccountFromDb != null) {
            return false;
        }

        patronAccount.setRole(1L);
        patronAccount.setStatus(true);
        patronAccount.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        patronAccount.setPassword(bCryptPasswordEncoder.encode(patronAccount.getPassword()));
        patronAccountRepository.save(patronAccount);
        return true;

    }

    public boolean deletePatronAccount(String id) {
        if (patronAccountRepository.findById(id).isPresent()) {
            patronAccountRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
