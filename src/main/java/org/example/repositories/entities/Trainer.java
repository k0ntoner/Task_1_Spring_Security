package org.example.repositories.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name="trainers")
@Data
public class Trainer extends User {
    @Enumerated(EnumType.STRING)
    private TrainingType trainingType;
    private String specialization;
    @ManyToMany
    @JoinTable(name="trainers_trainees",
            joinColumns = @JoinColumn(name="trainer_id"),
            inverseJoinColumns = @JoinColumn(name="trainee_id"))
    private List<Trainee> trainees;
    @OneToMany(mappedBy = "trainer")
    private List<Training> trainings;
}
