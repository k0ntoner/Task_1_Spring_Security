package org.example.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class TrainerDto extends UserDto {
    private String specialization;
    private Long userId;
    private TrainingTypeDto trainingType;
}
