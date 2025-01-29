package org.example.models.trainer;

import com.sun.istack.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.example.enums.TrainingType;
@Data
@Builder
public class TrainerUpdateDto {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String username;

    @NotNull
    @Builder.Default
    private boolean isActive = true;

    @NotNull
    private TrainingType specialization;
}
