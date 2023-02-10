package com.example.alleywayalliancelms.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {
    @Id
    @Column(name = "role_id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    /*
     *  -- Relationships:
     * Role(Many) - PatronAccount(One) --> Owner
     * ---------------------------------
     * 1 Owner, 0 Inverse
     * */

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    Set<PatronAccount> patronAccount;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(Long id) {
        this.id = id;
    }

    // Метод возвращает имя роли (должен соответствовать шаблону ROLE_ИМЯ)
    @Override
    public String getAuthority() {
        return getName();
    }
}