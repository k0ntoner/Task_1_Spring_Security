package org.example.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Training {
    private long traineeId;
    private long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime trainingDate;
    private Duration trainingDuration;
}

