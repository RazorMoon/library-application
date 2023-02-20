package com.example.alleywayalliancelms.repository;

import com.example.alleywayalliancelms.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findNotificationsByPatronId(String patronId);

}