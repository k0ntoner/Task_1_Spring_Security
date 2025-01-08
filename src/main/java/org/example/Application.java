package org.example;

import org.example.configs.Config;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingType;
import org.example.services.ServiceFacade;
import org.example.utils.ConsoleUI;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI(new AnnotationConfigApplicationContext(Config.class));
        consoleUI.start();
    }
}
