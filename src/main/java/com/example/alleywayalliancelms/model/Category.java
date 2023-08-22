package com.example.alleywayalliancelms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(generator = "category_category_id_seq")
    @Column(name = "category_id", nullable = false)
    private Long id;

    @NotNull(message = "Категория не должна быть пустой!")
    @Size(min = 2, message = "Категория должна иметь не менее двух символов!")
    @Column(name = "name")
    private String categoryName;

    /*
    *  -- Relationships:
    * Category(One) - Book(Many) --> Inverse
    * -------------------------
    * 0 Owner, 1 Inverse
    * */

    @OneToMany(mappedBy = "categories")
    @JsonIgnore
    Set<Book> books;

}