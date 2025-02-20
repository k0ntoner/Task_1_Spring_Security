package org.example.repositories.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "trainees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Trainee extends User {
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String address;

    @OneToMany(mappedBy = "trainee", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    @ToString.Exclude
    private Collection<Training> trainings = new ArrayList<>();

}
