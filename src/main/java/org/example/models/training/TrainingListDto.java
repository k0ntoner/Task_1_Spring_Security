package org.example.models.training;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingListDto {
    @NotNull
    @Builder.Default
    private Collection<TrainingViewDto> trainings = new ArrayList<>();
}
