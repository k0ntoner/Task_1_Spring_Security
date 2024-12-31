package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@SuperBuilder
public class Trainer extends User{
    private String specialization;
    private long userId;
    private TrainingType trainingType;
}
