package com.example.alleywayalliancelms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(generator = "notification_notification_id_seq")
    @Column(name = "notification_id", nullable = false)
    private Long id;

    @Column(name = "sent_at")
    private Date sentAt;

    @Column(name = "type")
    private String type;

    @Column(name = "patron_id")
    private String patronId;

    /*
     *  -- Relationships:
     * Notification(Many) - Patron_Account(one) --> Owner
     * ---------------------------------
     * 1 Owner, 0 Inverse
     * */

    @ManyToOne
    @JoinColumn(name = "patron_id", updatable = false, insertable = false)
    private PatronAccount patronAccount;

}