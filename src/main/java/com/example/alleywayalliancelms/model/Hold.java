package com.example.alleywayalliancelms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hold")
public class Hold {
    @Id
    @GeneratedValue(generator = "hold_hold_id_seq")
    @Column(name = "hold_id", nullable = false)
    private Long id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "book_copy_id")
    private Long copyId;

    @Column(name = "patron_account_id")
    private String patronId;

    /*
     * -- Relationships:
     * Book_Copy(One) - Hold(Many) --> Owner
     * Book_Copy(Many) - Patron_Account(One) --> Owner
     * ------------------------------------------
     * 2 Owner, 0 Inverse
     * */

    @ManyToOne
    @JoinColumn(name = "book_copy_id", insertable = false, updatable = false)
    BookCopy bookCopy;

    @ManyToOne
    @JoinColumn(name = "patron_account_id", insertable = false, updatable = false)
    PatronAccount patronAccount;

}