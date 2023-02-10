package com.example.alleywayalliancelms.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

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