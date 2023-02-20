package com.example.alleywayalliancelms.service;

import com.example.alleywayalliancelms.model.BookCopy;
import com.example.alleywayalliancelms.model.Hold;
import com.example.alleywayalliancelms.model.PatronAccount;
import com.example.alleywayalliancelms.repository.BookCopyRepository;
import com.example.alleywayalliancelms.repository.HoldRepository;
import com.example.alleywayalliancelms.repository.PatronAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class HoldService {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final HoldRepository holdRepository;

    private final BookCopyService bookCopyService;

    private final CheckoutService checkoutService;

    private final PatronAccountService patronAccountService;

    private final NotificationService notificationService;

    @Autowired
    public HoldService(HoldRepository holdRepository,
                       BookCopyService bookCopyService,
                       CheckoutService checkoutService,
                       PatronAccountService patronAccountService,
                       NotificationService notificationService) {
        this.holdRepository = holdRepository;
        this.bookCopyService = bookCopyService;
        this.checkoutService = checkoutService;
        this.patronAccountService = patronAccountService;
        this.notificationService = notificationService;
    }

    public boolean saveHold(Hold hold, Long CopyId, String patronEmail) {
        LocalDateTime date = LocalDateTime.now();

        LocalDateTime endDate = date.plusDays(5);

        Hold holdFromDb = holdRepository.findFirstByEndTimeAndBookCopyIdDesc(hold.getCopyId());


        if (holdFromDb != null) {
            hold.setId(holdRepository.getLastId() + 1);
            if (holdFromDb.getEndTime() == null) {
                createHold(hold, CopyId, patronEmail, endDate);
            } else {
                if (checkoutService.checkBookCopyAvialiability(hold.getCopyId())) {
                    if (!checkoutService.checkIfUserAlreadyHaveAnotherCopyOfTheBook(patronAccountService.getAccountByEmail(patronEmail).getId(), hold.getCopyId())) {
                        createHold(hold, CopyId, patronEmail, endDate);
                    } else return false;
                } else return false;
            }
        } else {
            if (!checkoutService.checkIfUserAlreadyHaveAnotherCopyOfTheBook(patronAccountService.getAccountByEmail(patronEmail).getId(), hold.getCopyId())) {
                if (holdRepository.getLastId() == null) {
                    hold.setId(1L);
                } else {
                    hold.setId(holdRepository.getLastId() + 1);
                }
                createHold(hold, CopyId, patronEmail, endDate);
            } else return false;
        }
        return true;
    }


    public List<Hold> getHoldsByPatronEmail(String email) {
        return holdRepository.findHoldsByPatronId(patronAccountService.getAccountByEmail(email).getId());
    }

    public List<Hold> getActiveHoldList(List<Hold> holds) {
        ArrayList<Hold> filteredHoldList = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now();

        for (Hold el : holds) {
            if (el.getEndTime().isAfter(today) && checkoutService.findCheckoutByHold(el, el.getPatronId(), false) != null
                    && checkoutService.findCheckoutByHold(el, el.getPatronId(), false).getEndDate() == null) {
                filteredHoldList.add(el);
            }
        }
        return filteredHoldList;
    }

    public List<Hold> getExpiredHoldList(List<Hold> holds) {
        ArrayList<Hold> filteredHoldList = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now();

        for (Hold el : holds) {
            if (today.isAfter(el.getEndTime()) || checkoutService.findCheckoutByHold(el, el.getPatronId(), true) != null) {
                filteredHoldList.add(el);
            }
        }
        return filteredHoldList;
    }

    private void createHold(Hold hold, Long CopyId, String patronEmail, LocalDateTime endDate) {
        BookCopy bookCopy = bookCopyService.getBookCopyById(CopyId);
        PatronAccount patronAccount = patronAccountService.getAccountByEmail(patronEmail);
        LocalDateTime now = LocalDateTime.now();


        hold.setStartTime(now);
        hold.setEndTime(endDate);

        hold.setPatronAccount(patronAccount);
        hold.setBookCopy(bookCopy);

        hold.setCopyId(bookCopy.getId());
        hold.setPatronId(patronAccount.getId());

        notificationService.addNotification(patronAccount.getId(), bookCopy.getId(), endDate);

        checkoutService.placeACheckout(hold);
        holdRepository.save(hold);
    }

}
