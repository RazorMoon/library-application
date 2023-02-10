package com.example.alleywayalliancelms.Service;

import com.example.alleywayalliancelms.Model.*;
import com.example.alleywayalliancelms.Repository.CheckoutRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class CheckoutService {

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Autowired
    private BookCopyService bookCopyService;

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

    public Checkout findCheckoutByDateAndPatronId(Date date, String accountId) {
        return checkoutRepository.findCheckoutByStartDateAndPatronAccountId(date, accountId);
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
        Date date = new Date();
        log.info("Checkout : {}", checkout);
        if (checkout != null) {
            if (checkout.getEndDate() == null) {
                checkout.setIsReturned(isReturned);
                checkout.setEndDate(date);
                checkoutRepository.save(checkout);
            }
        }
    }
}
