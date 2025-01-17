package org.example.repositories.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "trainings")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
    @NotNull
    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "training_type", nullable = false)
    private TrainingType trainingType;

    @NotNull
    @Column(name = "training_date", nullable = false)
    private LocalDateTime trainingDate;
    @NotNull
    @Column(name = "training_duration", nullable = false)
    private Duration trainingDuration;
}

