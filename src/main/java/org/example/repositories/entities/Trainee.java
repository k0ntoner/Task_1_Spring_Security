package org.example.repositories.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="trainees")
@Data
public class Trainee extends User {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String address;
    @ManyToMany(mappedBy = "trainee")
    private List<Trainer> trainers;
    @OneToMany(mappedBy = "trainees")
    private List<Training> trainings;

}
