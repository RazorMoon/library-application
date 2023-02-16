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
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(generator = "genre_genre_id_seq")
    @Column(name = "genre_id", nullable = false)
    private Long genreId;

    @NotNull(message = "Поле жанр не должно быть пустым!")
    @Size(min = 2, max = 40, message = "Поле жанр не должно быть меньше двух символов!")
    @Column(name = "name")
    private String name;

    /*
    * -- Relationships:
    * Genre(One) - Book(Many) --> Inverse side
    * ----------------------------------
    * 0 Owner, 1 Inverse
    * */

    @OneToMany(mappedBy = "genres")
    @JsonIgnore
    Set<Book> books;

}