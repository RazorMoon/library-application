package com.example.alleywayalliancelms.service;

import com.example.alleywayalliancelms.model.Checkout;
import com.example.alleywayalliancelms.model.Notification;
import com.example.alleywayalliancelms.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class NotificationService {

    public final NotificationRepository notificationRepository;

    public final PatronAccountService patronAccountService;

    public final CheckoutService checkoutService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               PatronAccountService patronAccountService,
                               CheckoutService checkoutService) {
        this.notificationRepository = notificationRepository;
        this.patronAccountService = patronAccountService;
        this.checkoutService = checkoutService;
    }

    public String convertPatronEmailToId(String email) {
        return patronAccountService.getAccountByEmail(email).getId();
    }

    public void addNotification(String patronId, Long bookCopyId, LocalDateTime endDate) {
        Notification notification = new Notification();

        notification.setPatronId(patronId);
        notification.setType("Return book copy: " + bookCopyId);
        notification.setSentAt(endDate);

        notificationRepository.save(notification);
    }


    public List<Notification> getNotificationsList(String patronId) {
        List<Notification> notifications = notificationRepository.findNotificationsByPatronId(patronId);
        List<Notification> sortedNotes = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        LocalDateTime today = LocalDateTime.now();

        for (Notification el : notifications) {
            calendar.setTime(java.sql.Timestamp.valueOf(el.getSentAt()));
            calendar.add(Calendar.HOUR, -72);
            Checkout checkout = checkoutService.findCheckoutByDateAndPatronId(el.getSentAt(), patronId);

            log.info("Checkout : {}", checkout);
            log.info("isReturned : {}", checkout.getIsReturned());

            if (today.isAfter(calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    && today.isBefore(el.getSentAt())
                    && !checkout.getIsReturned()) {
                sortedNotes.add(el);
            }
        }

        return sortedNotes;
    }

}
