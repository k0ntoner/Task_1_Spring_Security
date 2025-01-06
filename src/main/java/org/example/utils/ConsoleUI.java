package org.example.utils;

import org.example.configs.Config;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingType;
import org.example.services.ServiceFacade;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ConsoleUI {
    private ServiceFacade serviceFacade;
    private AnnotationConfigApplicationContext context;
    public ConsoleUI(AnnotationConfigApplicationContext context) {
        this.serviceFacade = context.getBean(ServiceFacade.class);
        this.context = context;
    }
    public void start(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n-------Commands--------");
            System.out.println("1. Add Trainee");
            System.out.println("2. Add Trainer");
            System.out.println("3. Add Training");
            System.out.println("4. Update Trainee");
            System.out.println("5. Update Trainer");
            System.out.println("6. Delete Trainee");
            System.out.println("7. Delete Trainer");
            System.out.println("8. Find Trainee by ID");
            System.out.println("9. Find Trainer by ID");
            System.out.println("10. Find Training by ID");
            System.out.println("11. Find Training by Trainer's ID and Date");
            System.out.println("12. Find Training by Trainee's ID and Date");
            System.out.println("13. Find All Trainees");
            System.out.println("14. Find All Trainings");
            System.out.println("15. Find All Trainers");
            System.out.println("16. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Trainee First Name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter Trainee Last Name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Enter Date of Birth (yyyy-MM-dd): ");
                    LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine(), dateFormatter);
                    System.out.print("Enter Address: ");
                    String address = scanner.nextLine();
                    Trainee trainee = Trainee.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .dateOfBirth(dateOfBirth)
                            .address(address)
                            .isActive(true)
                            .build();
                    serviceFacade.addTrainee(trainee);
                    System.out.println("Trainee added successfully!");
                }
                case 2 -> {
                    System.out.print("Enter Trainer First Name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter Trainer Last Name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Enter Specialization: ");
                    String specialization = scanner.nextLine();
                    System.out.print("Enter Training Type (STRENGTH, CARDIO, FLEXIBILITY, BALANCE, ENDURANCE): ");
                    TrainingType trainingType = TrainingType.valueOf(scanner.nextLine().toUpperCase());
                    Trainer trainer = Trainer.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .specialization(specialization)
                            .trainingType(trainingType)
                            .isActive(true)
                            .build();
                    serviceFacade.addTrainer(trainer);
                    System.out.println("Trainer added successfully!");
                }
                case 3 -> {
                    System.out.print("Enter Trainee ID: ");
                    long traineeId = scanner.nextLong();
                    System.out.print("Enter Trainer ID: ");
                    long trainerId = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("Enter Training Name: ");
                    String trainingName = scanner.nextLine();
                    System.out.print("Enter Training Type (STRENGTH, CARDIO, FLEXIBILITY, BALANCE, ENDURANCE): ");
                    TrainingType trainingType = TrainingType.valueOf(scanner.nextLine().toUpperCase());
                    System.out.print("Enter Training Date and Time (yyyy-MM-dd'T'HH:mm): ");
                    LocalDateTime trainingDate = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
                    System.out.print("Enter Training Duration in minutes: ");
                    Duration trainingDuration = Duration.ofMinutes(scanner.nextLong());
                    Training training = Training.builder()
                            .traineeId(traineeId)
                            .trainerId(trainerId)
                            .trainingName(trainingName)
                            .trainingType(trainingType)
                            .trainingDate(trainingDate)
                            .trainingDuration(trainingDuration)
                            .build();
                    serviceFacade.addTraining(training);
                    System.out.println("Training added successfully!");
                }
                case 4 -> {
                    System.out.print("Enter Trainee ID to Update: ");
                    long id = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("Enter New Address: ");
                    String address = scanner.nextLine();
                    Trainee updatedTrainee = serviceFacade.findTraineeById(id);
                    if (updatedTrainee != null) {
                        updatedTrainee.setAddress(address);
                        serviceFacade.updateTrainee(updatedTrainee);
                        System.out.println("Trainee updated successfully!");
                    } else {
                        System.out.println("Trainee not found.");
                    }
                }
                case 5 -> {
                    System.out.print("Enter Trainer ID to Update: ");
                    long id = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("Enter New Specialization: ");
                    String specialization = scanner.nextLine();
                    Trainer updatedTrainer = serviceFacade.findTrainerById(id);
                    if (updatedTrainer != null) {
                        updatedTrainer.setSpecialization(specialization);
                        serviceFacade.updateTrainer(updatedTrainer);
                        System.out.println("Trainer updated successfully!");
                    } else {
                        System.out.println("Trainer not found.");
                    }
                }
                case 6 -> {
                    System.out.print("Enter Trainee ID to Delete: ");
                    long id = scanner.nextLong();
                    serviceFacade.deleteTrainee(serviceFacade.findTraineeById(id));
                    System.out.println("Trainee deleted successfully!");
                }
                case 7 -> {
                    System.out.println("Unsupported operation!");
                }
                case 8 -> {
                    System.out.print("Enter Trainee ID: ");
                    long id = scanner.nextLong();
                    System.out.println(serviceFacade.findTraineeById(id).toString());
                }
                case 9 -> {
                    System.out.print("Enter Trainer ID: ");
                    long id = scanner.nextLong();
                    System.out.println(serviceFacade.findTrainerById(id).toString());
                }
                case 10 -> {
                    System.out.print("Enter Training ID: ");
                    long id = scanner.nextLong();
                    System.out.println(serviceFacade.findTrainingById(id).toString());
                }
                case 11 -> {
                    System.out.print("Enter trainer ID: ");
                    long trainerId = scanner.nextLong();
                    System.out.print("Enter training date (yyyy-MM-ddTHH:mm): ");
                    scanner.nextLine();
                    LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine());
                    System.out.println(serviceFacade.findTrainingByTrainer(trainerId, dateTime).toString());
                }
                case 12 -> {
                    System.out.print("Enter trainee ID: ");
                    long traineeId = scanner.nextLong();
                    System.out.print("Enter training date (yyyy-MM-ddTHH:mm): ");
                    scanner.nextLine();
                    LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine());
                    System.out.println(serviceFacade.findTrainingByTrainee(traineeId, dateTime).toString());
                }
                case 13 -> {
                    System.out.println("All Trainees:");
                    serviceFacade.findAllTrainees().forEach(entity -> System.out.println(entity.toString()));

                }
                case 14 -> {
                    System.out.println("All Trainers:");
                    serviceFacade.findAllTrainers().forEach(entity -> System.out.println(entity.toString()));
                }
                case 15 -> {
                    System.out.println("All Trainings:");
                    serviceFacade.findAllTrainings().forEach(entity -> System.out.println(entity.toString()));
                }
                case 16 -> {
                    System.out.println("Exiting...");
                    context.close();
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

}
