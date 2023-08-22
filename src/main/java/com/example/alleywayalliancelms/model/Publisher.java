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
@Table(name = "publisher")
public class Publisher {
    @Id
    @GeneratedValue(generator = "publisher_publisher_id_seq")
    @Column(name = "publisher_id", nullable = false)
    private Long id;

    @NotNull(message = "Поле наименование не должно быть пустым!")
    @Size(min = 2, message = "Поле наименование должно содержать не менее двух символов!")
    @Column(name = "name")
    private String name;

    /*
     *  -- Relationships:
     * Book_Copy(Many) - Publisher(One) --> Inverse
     * ---------------------------------
     * 0 Owner, 1 Inverse
     * */

    @OneToMany(mappedBy = "publisher")
    @JsonIgnore
    Set<BookCopy> bookCopySet;

}