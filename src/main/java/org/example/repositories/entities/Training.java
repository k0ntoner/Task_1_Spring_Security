package org.example.repositories.entities;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.example.enums.TrainingType;

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
    @JoinColumn(name = "trainee_id", nullable = false)
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @Column(name = "name", nullable = false)
    private String trainingName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TrainingType trainingType;

    @Column(name = "date", nullable = false)
    private LocalDateTime trainingDate;

    @Column(name = "duration", nullable = false)
    private Duration trainingDuration;
}

