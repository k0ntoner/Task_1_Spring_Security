package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TrainingTypeDto {
    STRENGTH,
    CARDIO,
    FLEXIBILITY,
    BALANCE,
    ENDURANCE;

}
