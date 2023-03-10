package com.example.alleywayalliancelms.Repository;

import com.example.alleywayalliancelms.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findNotificationsByPatronId(String patronId);

}