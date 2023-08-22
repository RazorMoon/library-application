package com.example.alleywayalliancelms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "checkout")
public class Checkout {
    @Id
    @GeneratedValue(generator = "checkout_checkout_id_seq")
    @Column(name = "checkout_id")
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_returned")
    private Boolean isReturned;

    @Column(name = "book_copy_id")
    private Long bookId;

    @Column(name = "patron_account_id")
    private String patronAccountId;

    /*
    *  -- Relationships
    * Book_Copy(One) - Checkout(Many) --> Owner
    * Checkout(Many) - Patron_Account(One) --> Owner
    * -----------------------------------
    * 2 Owner, 0 Inverse
    * */

    @ManyToOne
    @JoinColumn(name = "book_copy_id", updatable = false, insertable = false)
    private BookCopy bookCopies;

    @ManyToOne
    @JoinColumn(name = "patron_account_id", updatable = false, insertable = false)
    private PatronAccount patronAccount;

}