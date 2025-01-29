package org.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.TrainingType;
import org.example.models.trainee.TraineeRegistrationDto;
import org.example.models.trainee.TraineeUpdateDto;
import org.example.models.training.TrainingListViewDto;
import org.example.models.user.ChangeUserPasswordDto;
import org.example.models.user.LoginUserDto;
import org.example.models.trainee.TraineeDto;
import org.example.models.training.TrainingDto;
import org.example.models.trainee.TraineeViewDto;
import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.example.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainees")
@Slf4j
public class TraineeController {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingService trainingService;


    @Autowired
    private ConversionService conversionService;

    @PostMapping("/registration")
    public ResponseEntity<?> createTrainee(@RequestBody TraineeRegistrationDto traineeRegistrationDto) {
        try{
            TraineeDto traineeDto= conversionService.convert(traineeRegistrationDto, TraineeDto.class);
            TraineeDto savedTraineeDto = traineeService.add(traineeDto);

            LoginUserDto loginUserDto = conversionService.convert(savedTraineeDto, LoginUserDto.class);

            EntityModel<LoginUserDto> entityModel = EntityModel.of(loginUserDto);

            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                    .methodOn(TraineeController.class)
                    .createTrainee(traineeRegistrationDto))
                    .withSelfRel());

            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                    .methodOn(TraineeController.class)
                    .getTrainee(savedTraineeDto.getUsername()))
                    .withRel("trainee"));

            URI location =ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();
            return ResponseEntity.created(location).body(entityModel);
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/trainee/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangeUserPasswordDto changeUserPasswordDto) {
        try{
            traineeService.changePassword(changeUserPasswordDto.getUsername(), changeUserPasswordDto.getOldPassword(), changeUserPasswordDto.getNewPassword());
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/trainee/{username}")
    public ResponseEntity<?> getTrainee(@PathVariable("username") String username) {
        Optional<TraineeDto> traineeDto=traineeService.findByUsername(username);
        if(traineeDto.isPresent()){
            TraineeViewDto traineeViewDto = conversionService.convert(traineeDto.get(), TraineeViewDto.class);
            EntityModel<TraineeViewDto> traineeEntityModel = EntityModel.of(traineeViewDto);

            traineeEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                    .methodOn(TraineeController.class)
                    .getTrainee(username))
                    .withSelfRel());

            return ResponseEntity.ok(traineeEntityModel);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<?> updateTrainee(@RequestBody TraineeUpdateDto traineeUpdateDto) {
        try {
            Optional<TraineeDto> foundTraineeDto = traineeService.findByUsername(traineeUpdateDto.getUsername());
            if(foundTraineeDto.isPresent()){
                TraineeDto updatedTraineeDto=foundTraineeDto.get();
                updatedTraineeDto.setUsername(traineeUpdateDto.getUsername());
                updatedTraineeDto.setFirstName(traineeUpdateDto.getFirstName());
                updatedTraineeDto.setLastName(traineeUpdateDto.getLastName());
                if(traineeUpdateDto.getAddress() != null){
                    updatedTraineeDto.setAddress(traineeUpdateDto.getAddress());
                }
                if(traineeUpdateDto.getDateOfBirth() != null){
                    updatedTraineeDto.setDateOfBirth(traineeUpdateDto.getDateOfBirth());
                }
                updatedTraineeDto = traineeService.update(updatedTraineeDto);

                TraineeViewDto traineeViewDto= conversionService.convert(updatedTraineeDto, TraineeViewDto.class);

                EntityModel<TraineeViewDto> traineeEntityModel = EntityModel.of(traineeViewDto);

                traineeEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                                .methodOn(TraineeController.class)
                                .updateTrainee(traineeUpdateDto))
                        .withSelfRel());

                return ResponseEntity.ok(traineeEntityModel);
            }
            throw new IllegalArgumentException("Invalid username");
        }
        catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteTrainee(@PathVariable String username) {
        Optional<TraineeDto> foundTraineeDto=traineeService.findByUsername(username);
        if(foundTraineeDto.isPresent()){
            traineeService.delete(foundTraineeDto.get());
        }

        String selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                        .methodOn(TraineeController.class)
                        .deleteTrainee(username))
                .withSelfRel().getHref();
        return ResponseEntity.noContent().header("Link", "<"+selfLink+">; rel=\"self\"").build();
    }
    @PutMapping("/trainee/{username}")
    public ResponseEntity<?> updateTrainings(@PathVariable String username,@RequestBody Collection<TrainingListViewDto> trainingListViewDtos) {
        try{
            Optional<TraineeDto> traineeDto=traineeService.findByUsername(username);
            if(traineeDto.isPresent()){
                TraineeDto traineeDtoToUpdate=traineeDto.get();
                Collection<TrainingDto> trainingDtos=trainingListViewDtos.stream().map(trainingListViewDto -> {
                    return TrainingDto.builder()
                            .trainingDuration(trainingListViewDto.getTrainingDuration())
                            .trainingName(trainingListViewDto.getTrainingName())
                            .trainingType(trainingListViewDto.getTrainingType())
                            .trainingDate(trainingListViewDto.getTrainingDateTime())
                            .trainerDto(trainerService.findByUsername(trainingListViewDto.getTrainerUsername()).get())
                            .traineeDto(traineeDtoToUpdate)
                            .build();
                }).collect(Collectors.toList());

                traineeDtoToUpdate.setTrainings(trainingDtos);

                TraineeDto updatedTraineeDto=traineeService.update(traineeDtoToUpdate);

                EntityModel<Collection<TrainingDto>> entityModel = EntityModel.of(updatedTraineeDto.getTrainings());

                entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                                .methodOn(TraineeController.class)
                                .updateTrainings(username,trainingListViewDtos))
                        .withSelfRel());
                return ResponseEntity.ok(entityModel);
            }
            throw new IllegalArgumentException("Invalid username");
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping("/trainee/{username}/trainings")
    public ResponseEntity<?> getTrainings(@PathVariable String username,
                                          @RequestParam(required = false) LocalDateTime periodFrom,
                                          @RequestParam(required = false) LocalDateTime periodTo,
                                          @RequestParam(required = false) String trainerUsername,
                                          @RequestParam(required = false) TrainingType trainingType) {

        if(traineeService.findByUsername(username).isPresent()){
            Collection<TrainingListViewDto> trainingListViewDtos=trainingService.findByTrainee(username, periodFrom, periodTo, trainerUsername, trainingType).stream()
                    .map(trainingDto -> TrainingListViewDto.builder()
                            .traineeUsername(username)
                            .trainerUsername(trainingDto.getTrainerDto().getUsername())
                            .trainingDateTime(trainingDto.getTrainingDate())
                            .trainingDuration(trainingDto.getTrainingDuration())
                            .trainingName(trainingDto.getTrainingName())
                            .trainingType(trainingDto.getTrainingType())
                            .build()).collect(Collectors.toList());

            EntityModel<Collection<TrainingListViewDto>> entityModel = EntityModel.of(trainingListViewDtos);

            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                            .methodOn(TraineeController.class)
                            .getTrainings(username, periodFrom, periodTo, trainerUsername, trainingType))
                    .withSelfRel());

            return ResponseEntity.ok(entityModel);
        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/trainee/{username}/de-activate")
    public ResponseEntity<?> de_activate(@PathVariable String username, @RequestParam boolean activate) {
        Optional<TraineeDto> traineeDto=traineeService.findByUsername(username);
        if(traineeDto.isPresent()){
            if(activate){
                traineeService.activate(traineeService.findByUsername(username).get());
            }
            else{
                traineeService.deactivate(traineeService.findByUsername(username).get());
            }
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
