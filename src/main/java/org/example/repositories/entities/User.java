package org.example.repositories.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name="active", nullable = false)
    private boolean isActive;
}
