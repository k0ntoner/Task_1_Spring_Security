package org.example.repositories.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.enums.TrainingType;

import java.util.ArrayList;
import java.util.Collection;
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
    private TrainingType specialization;

    @OneToMany(mappedBy = "trainer", cascade = {CascadeType.REMOVE,CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private Collection<Training> trainings=new ArrayList<>();
}
