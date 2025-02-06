package org.example.models.training;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.TrainingType;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeListDto {
    @NotNull
    @Builder.Default
    private Collection<TrainingType> trainingTypes = new ArrayList<>();
}
