package org.example.models.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.models.trainer.TrainerDto;
import org.example.models.training.TrainingDto;
import org.example.models.user.UserDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class TraineeDto extends UserDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String address;

    @NotNull
    @Builder.Default
    private Collection<TrainingDto> trainings = new ArrayList<>();

    @NotNull
    @Builder.Default
    private Collection<TrainerDto> trainers = new ArrayList<>();
}
