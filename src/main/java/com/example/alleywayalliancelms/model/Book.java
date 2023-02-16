package com.example.alleywayalliancelms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(generator = "book_book_id_seq")
    @Column(name = "book_id", nullable = false)
    private Long id;

    @NotNull(message = "У книги должно быть название!")
    @Size(min = 2, message = "Название должно быть не меньше 2 символов!")
    @Column(name = "title")
    private String title;

    @Column(name = "genre")
    private Integer genre;

    @NotNull(message = "Укажите допустимый возраст!")
    @Digits(integer = 2, fraction = 0, message = "Разрешены только двузначные числа!")
    @Column(name = "allowed_age")
    private Integer allowedAge;

    @Column(name = "category")
    private Long category;

    /*
     * -- Relationships:
     * Book(Many) - Author(Many) --> Owner
     * Book(One) - Book_Copy(Many) --> Inverse
     * Book(Many) - Patron_Account(Many) (through SERVICE table "Wait-list") --> Inverse
     * Book(Many) - Genre(One) --> Owner
     * -------------------------------------
     * 2 Owner, 2 Inverse
     * */

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL, CascadeType.REMOVE})
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    Set<Author> authors;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    @JsonIgnore
    Set<BookCopy> bookCopies;

    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER, cascade = {CascadeType.ALL, CascadeType.REMOVE})
    @JsonIgnore
    Set<PatronAccount> waitlistpatronAccounts;

    @ManyToOne
    @JoinColumn(name = "category", insertable = false, updatable = false)
    Category categories;

    @ManyToOne
    @JoinColumn(name = "genre", insertable = false, updatable = false)
    Genre genres;


}