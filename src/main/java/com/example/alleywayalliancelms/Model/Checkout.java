package com.example.alleywayalliancelms.Model;

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
@Table(name = "checkout")
public class Checkout {
    @Id
    @GeneratedValue(generator = "checkout_checkout_id_seq")
    @Column(name = "checkout_id")
    private Long id;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

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