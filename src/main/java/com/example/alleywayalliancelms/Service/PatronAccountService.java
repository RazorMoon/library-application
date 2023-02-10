package com.example.alleywayalliancelms.Service;

import com.example.alleywayalliancelms.Exception.BookNotFoundException;
import com.example.alleywayalliancelms.Model.Book;
import com.example.alleywayalliancelms.Model.BookCopy;
import com.example.alleywayalliancelms.Model.PatronAccount;
import com.example.alleywayalliancelms.Model.Role;
import com.example.alleywayalliancelms.Repository.BookCopyRepository;
import com.example.alleywayalliancelms.Repository.PatronAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class PatronAccountService implements UserDetailsService {


    @Autowired
    private PatronAccountRepository patronAccountRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<PatronAccount> getAllAccounts() {
        return patronAccountRepository.findAll();
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

    public PatronAccount save(PatronAccount patronAccount) {
        return patronAccountRepository.save(patronAccount);
    }

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

        //return patronAccount;
    }

    public PatronAccount getAuthentificatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

        }
        return patronAccountRepository.getPatronAccountByEmail(authentication.getName());
    }

    public Long getCountOfAllAccounts() {
        return patronAccountRepository.count();
    }
}
