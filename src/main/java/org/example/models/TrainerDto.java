package org.example.models;

import com.sun.istack.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.enums.TrainingType;

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
}
