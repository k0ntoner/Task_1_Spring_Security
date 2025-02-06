package org.example.models.trainer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.models.training.TrainingViewDto;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerListDto {
    @NotNull
    @Builder.Default
    private Collection<TrainerListViewDto> trainers = new ArrayList<>();
}
