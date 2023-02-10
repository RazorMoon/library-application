package com.example.alleywayalliancelms.Service;

import com.example.alleywayalliancelms.Model.BookCopy;
import com.example.alleywayalliancelms.Model.Hold;
import com.example.alleywayalliancelms.Model.PatronAccount;
import com.example.alleywayalliancelms.Repository.BookCopyRepository;
import com.example.alleywayalliancelms.Repository.HoldRepository;
import com.example.alleywayalliancelms.Repository.PatronAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class HoldService {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private HoldRepository holdRepository;

    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Autowired
    private PatronAccountRepository patronAccountRepository;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private PatronAccountService patronAccountService;

    @Autowired
    private NotificationService notificationService;


    public List<Hold> getAllHoldsList() {
        return holdRepository.findAll();
    }

    public Hold save(Hold hold) {
        return holdRepository.save(hold);
    }


    public boolean saveHold(Hold hold, Long CopyId, String patronEmail) throws IllegalArgumentException {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 120);
        Date endDate = calendar.getTime();
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
        Date today = new Date();

        for (Hold el : holds) {
            if (el.getEndTime().after(today) && checkoutService.findCheckoutByHold(el, el.getPatronId(), false) != null
                    && checkoutService.findCheckoutByHold(el, el.getPatronId(), false).getEndDate() == null) {
                filteredHoldList.add(el);
            }
        }
        return filteredHoldList;
    }

    public List<Hold> getExpiredHoldList(List<Hold> holds) {
        ArrayList<Hold> filteredHoldList = new ArrayList<>();
        Date today = new Date();

        for (Hold el : holds) {
            if (today.after(el.getEndTime()) || checkoutService.findCheckoutByHold(el, el.getPatronId(), true) != null) {
                filteredHoldList.add(el);
            }
        }
        return filteredHoldList;
    }

    private void createHold(Hold hold, Long CopyId, String patronEmail, Date endDate) throws IllegalArgumentException {
        BookCopy bookCopy = bookCopyRepository.findById(CopyId).get();
        PatronAccount patronAccount = patronAccountRepository.getPatronAccountByEmail(patronEmail);

        Date now = new Date();
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
