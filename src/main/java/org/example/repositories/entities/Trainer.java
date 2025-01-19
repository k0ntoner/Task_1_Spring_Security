package org.example.repositories.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.enums.TrainingType;

import java.util.List;

@Entity
@Table(name = "trainers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Trainer extends User {
    @Enumerated(EnumType.STRING)
    @Column(name = "training_type", nullable = false)
    private TrainingType trainingType;

    private String specialization;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings;
}
