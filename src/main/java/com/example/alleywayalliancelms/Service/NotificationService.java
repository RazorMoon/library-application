package com.example.alleywayalliancelms.Service;

import com.example.alleywayalliancelms.Model.Checkout;
import com.example.alleywayalliancelms.Model.Hold;
import com.example.alleywayalliancelms.Model.Notification;
import com.example.alleywayalliancelms.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    public NotificationRepository notificationRepository;

    @Autowired
    public PatronAccountService patronAccountService;

    @Autowired
    public CheckoutService checkoutService;

    public String convertPatronEmailToId(String email) {
        return patronAccountService.getAccountByEmail(email).getId();
    }

    public Notification addNotification(String patronId, Long bookCopyId, Date endDate) {
        Date today = new Date();
        Notification notification = new Notification();

        notification.setPatronId(patronId);
        notification.setType("Return book copy: " + bookCopyId);
        notification.setSentAt(endDate);

        return notificationRepository.save(notification);
    }


    public List<Notification> getNotificationsList(String patronId) {
        List<Notification> notifications = notificationRepository.findNotificationsByPatronId(patronId);
        List<Notification> sortedNotes = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();

        for (Notification el : notifications) {
            calendar.setTime(el.getSentAt());
            calendar.add(Calendar.HOUR, -72);
            Checkout checkout = checkoutService.findCheckoutByDateAndPatronId(el.getSentAt(), patronId);

            if (today.after(calendar.getTime()) && today.before(el.getSentAt()) && !checkout.getIsReturned()) {
                sortedNotes.add(el);
            }
        }

        return sortedNotes;
    }

    public List<Notification> getNotificationsByPatronAccountId(String patronAccountId) {
        return notificationRepository.findNotificationsByPatronId(patronAccountId);
    }

    public String convertPatronIdToEmail(String patronId) {
        return patronAccountService.findPatronAccountById(patronId).getEmail();
    }

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }




}
