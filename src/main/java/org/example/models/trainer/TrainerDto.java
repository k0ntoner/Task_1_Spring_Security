package org.example.models.trainer;

import com.sun.istack.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.enums.TrainingType;
import org.example.models.trainee.TraineeDto;
import org.example.models.training.TrainingDto;
import org.example.models.user.UserDto;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class TrainerDto extends UserDto {
    @NotNull
    private TrainingType specialization;

    @NotNull
    @Builder.Default
    private Collection<TrainingDto> trainings = new ArrayList<>();

    @NotNull
    @Builder.Default
    private Collection<TraineeDto> trainees = new ArrayList<>();
}
