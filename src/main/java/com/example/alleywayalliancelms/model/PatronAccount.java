package com.example.alleywayalliancelms.model;

import com.example.alleywayalliancelms.generator.SequenceGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@Slf4j
@Getter
@Setter
@Entity
@Table(name = "patron_account")
public class PatronAccount implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq")
    @GenericGenerator(name = "card_seq",
            strategy = "com.example.alleywayalliancelms.generator.SequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = SequenceGenerator.VALUE_PREFIX_PARAMETER, value = "AAU_"),
                    @org.hibernate.annotations.Parameter(name = SequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%06d")
            }
    )
    @Column(name = "card_number", nullable = false)
    private String id;

    @Size(min = 2, message = "Имя пользователя должно быть не меньше 2 знаков")
    @Column(name = "first_name")
    private String firstName;

    @Size(min = 2, message = "Фамилия пользователя должна быть не меньше 2 знаков")
    @Column(name = "surename")
    private String surename;

    @Size(min = 5, message = "Почта пользователя должна содержать не меньше 5 знаков")
    @NotNull(message = "У пользователя должна быть почта")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Это должна быть почта!")
    @Column(name = "email", unique = true)
    private String email;
    @Size(min = 5, message = "Пароль должен содержать не меньше 5 знаков")
    @NotNull(message = "Пароль не должен быть пустым")
    @Column(name = "password", columnDefinition = "varchar(255)")
    private String password;

    @Column(name = "role")
    private Long role;

    @Column(name = "status")
    private boolean status;

    @Transient
    private String passwordConfirm;

    /*
     * -- Relationships:
     * Patron_Account(One) - Role(One) --> Owner(?)
     * Patron_Account(One) - Notification(Many) --> Inverse
     * Patron_Account(One) - Checkout(Many) --> Inverse
     * Patron_Account(One) - Hold(Many) -- Inverse
     * Patron_Account(Many) - Book(Many)(through SERVICE table "Wait-list") --> Owner(?)
     * ----------------------------------------------
     * 2 Owner(?), 3 Inverse
     * */

    @ManyToMany
    @JoinTable(name = "waitlist",
            joinColumns = @JoinColumn(name = "patron_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    Set<Book> books;

    @OneToMany(mappedBy = "patronAccount")
    @JsonIgnore
    Set<Checkout> checkouts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "accounts_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles;

    @OneToMany(mappedBy = "patronAccount")
    @JsonIgnore
    Set<Hold> holds;

    @OneToMany(mappedBy = "patronAccount")
    @JsonIgnore
    Set<Notification> notifications;

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role el : getRoles()) {
            authorities.add(new SimpleGrantedAuthority(el.getName()));
        }

        log.info("Authority Roles : {}", roles.toString());
        //return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}