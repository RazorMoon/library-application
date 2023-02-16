package com.example.alleywayalliancelms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "book_copy")
public class BookCopy {

    @Id
    @GeneratedValue(generator = "book_copy_book_copy_id_seq")
    @Column(name = "book_copy_id", nullable = false)
    private Long id;

    @NotNull(message = "Поле год издания не должно быть пустым!")
    @Digits(integer = 4, fraction = 0, message = "Поле год издания должно иметь как минимум 4 цифры!")
    @Column(name = "year_published")
    private Long publishYear;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "publisher_id")
    private Long publishId;

    /*
    * Relationships:
    * Book(One) - Book_Copy(Many) --> Owner
    * Publisher(One) - Book_Copy(Many) --> Owner
    * Book_Copy(One) - Checkout(Many) --> Inverse
    * Book_Copy(One) - Hold(Many) --> Inverse
    * Book_Copy(Many) - Publisher(One) --> Owner
    * ----------------------------------------
    * 3 Owner, 2 Inverse
    * */

    @ManyToOne
    @JoinColumn(name = "book_id", updatable = false, insertable = false)
    private Book book;

    @OneToMany(mappedBy = "bookCopies", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Checkout> checkouts;

    @OneToMany(mappedBy = "bookCopy", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Hold> holds;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "publisher_id", updatable = false, insertable = false)
    private Publisher publisher;
}