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


@Getter
@Setter
@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(generator = "author_author_id_seq")
    @Column(name = "author_id", nullable = false)
    private Long id;

    @NotNull(message = "Имя автора не должно быть пустым!")
    @Size(min = 4, message = "Имя автора должно состоять, как минимум из четырех символов!")
    @Column(name = "name")
    private String name;

    /*
    * -- Relationships:
    * Book - Author (Inverse)
    * */

    @ManyToMany(mappedBy = "authors", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<Book> books;

}