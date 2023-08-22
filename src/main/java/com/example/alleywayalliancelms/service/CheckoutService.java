package com.example.alleywayalliancelms.service;

import com.example.alleywayalliancelms.exception.CheckoutNotFoundException;
import com.example.alleywayalliancelms.model.*;
import com.example.alleywayalliancelms.repository.CheckoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CheckoutRepository checkoutRepository;

    private final BookCopyService bookCopyService;


    public List<Checkout> getAllCheckouts() {
        return checkoutRepository.findAll();
    }

    public Checkout findCheckoutByHold(Hold hold, String patronAccountId, Boolean isReturned) {
        return checkoutRepository.findCheckoutByIsReturnedAndPatronAccountIdAndStartDate(isReturned, patronAccountId, hold.getEndTime());
    }

    public List<Checkout> findCheckoutsByAccount(String patronAccountId) {
        return checkoutRepository.findCheckoutByPatronAccountId(patronAccountId);
    }

    public boolean checkBookCopyAvialiability(Long copyId) {
        return checkoutRepository.checkIfCopyIsAvailible(copyId) <= 0;
    }

    public boolean checkIfUserAlreadyHaveAnotherCopyOfTheBook(String patronAccountId, Long bookCopyId) {
        Long bookId = bookCopyService.getBookCopyById(bookCopyId).getBookId();
        List<BookCopy> bookCopies = bookCopyService.getBookCopiesByBookId(bookId);

        for (BookCopy el : bookCopies) {
            if (checkoutRepository.checkIfUserAlreadyHaveAnotherCopyOfBookQuery(el.getId(), patronAccountId) > 0) {
                return true;
            }
        }
        return false;
    }

    public Set<Checkout> checkActiveCheckoutsForUser(PatronAccount patronAccount, Boolean isReturned) {
        return new HashSet<>(checkoutRepository.findCheckoutByIsReturnedAndPatronAccount(isReturned, patronAccount));
    }

    public Checkout findCheckoutByDateAndPatronId(LocalDateTime date, String accountId) {
        if (checkoutRepository.findCheckoutByStartDateAndPatronAccountId(date, accountId) != null) {
            return checkoutRepository.findCheckoutByStartDateAndPatronAccountId(date, accountId);
        } else {
            log.error("Checkout with given date : {} and account ID : {} is not found ", date, accountId);
            throw new CheckoutNotFoundException("Checkout with given date and account ID, is not found.");
        }
    }

    public void placeACheckout(Hold hold) {
        Checkout checkout = new Checkout();

        checkout.setStartDate(hold.getEndTime());
        checkout.setIsReturned(false);
        checkout.setBookCopies(hold.getBookCopy());
        checkout.setPatronAccount(hold.getPatronAccount());

        checkout.setBookId(hold.getCopyId());
        checkout.setPatronAccountId(hold.getPatronId());
        checkoutRepository.save(checkout);
    }

    public void checkoutComplete(Long bookCopyId, Boolean isReturned) {
        Checkout checkout = checkoutRepository.findCheckoutByBookIdAndEndDate(bookCopyId, null);
        LocalDateTime date = LocalDateTime.now();

        if (checkout != null && checkout.getEndDate() == null) {
            checkout.setIsReturned(isReturned);
            checkout.setEndDate(date);
            checkoutRepository.save(checkout);
        }
    }
}
