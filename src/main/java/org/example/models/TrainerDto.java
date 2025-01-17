package org.example.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.repositories.entities.TrainingType;

@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class TrainerDto extends UserDto {
    private String specialization;
    private Long userId;
    private TrainingType trainingType;
}
