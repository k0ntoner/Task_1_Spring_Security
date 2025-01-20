package org.example.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.repositories.entities.Training;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class TraineeDto extends UserDto {
    private LocalDate dateOfBirth;

    private String address;

    @NotNull
    @Builder.Default
    private Collection<TrainingDto> trainings = new ArrayList<>();
}
