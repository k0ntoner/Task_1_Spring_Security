package org.example.repositories.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

public enum TrainingType {
    STRENGTH,
    CARDIO,
    FLEXIBILITY,
    BALANCE,
    ENDURANCE;

}
