package org.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.models.trainee.TraineeDto;
import org.example.models.trainer.*;
import org.example.models.training.TrainingListViewDto;
import org.example.models.user.ChangeUserPasswordDto;
import org.example.models.user.LoginUserDto;
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
@RequestMapping("/trainers")
@Slf4j
public class TrainerController {
    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private ConversionService conversionService;

    @GetMapping
    public Collection<TrainerDto> getAllTrainers() {
        return trainerService.findAll();
    }

    @GetMapping("/trainer/{username}")
    public ResponseEntity<?> getTrainerByUsername(@PathVariable("username") String username) {
        Optional<TrainerDto> traineeDto = trainerService.findByUsername(username);

        if (traineeDto.isPresent()) {
            TrainerViewDto trainerViewDto = conversionService.convert(traineeDto.get(), TrainerViewDto.class);

            EntityModel<TrainerViewDto> entityModel = EntityModel.of(trainerViewDto);
            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                            .methodOn(TrainerController.class)
                            .getTrainerByUsername(username))
                    .withSelfRel());
            return ResponseEntity.ok(entityModel);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createTrainer(@RequestBody TrainerRegistrationDto trainerRegistrationDto) {
        try {
            TrainerDto trainerDto = conversionService.convert(trainerRegistrationDto, TrainerDto.class);
            TrainerDto savedTrainerDto = trainerService.add(trainerDto);

            LoginUserDto loginUserDto = conversionService.convert(savedTrainerDto, LoginUserDto.class);

            EntityModel<LoginUserDto> entityModel = EntityModel.of(loginUserDto);

            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                            .methodOn(TrainerController.class)
                            .createTrainer(trainerRegistrationDto))
                    .withSelfRel());

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/trainer/{username}").buildAndExpand(savedTrainerDto.getUsername()).toUri();

            return ResponseEntity.created(location).body(entityModel);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/trainer/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangeUserPasswordDto changeUserPasswordDto) {
        try {
            trainerService.changePassword(changeUserPasswordDto.getUsername(), changeUserPasswordDto.getOldPassword(), changeUserPasswordDto.getNewPassword());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/trainer")
    public ResponseEntity<?> updateTrainer(@RequestBody TrainerUpdateDto trainerDto) {
        try {
            Optional<TrainerDto> trainerDtoOptional = trainerService.findByUsername(trainerDto.getUsername());
            if (trainerDtoOptional.isPresent()) {
                TrainerDto trainerDtoToUpdate = trainerDtoOptional.get();

                trainerDtoToUpdate.setFirstName(trainerDto.getFirstName());
                trainerDtoToUpdate.setLastName(trainerDto.getLastName());
                trainerDtoToUpdate.setActive(trainerDto.isActive());

                TrainerDto updatedTrainerDto = trainerService.update(trainerDtoToUpdate);

                TrainerViewDto trainerViewDto = conversionService.convert(updatedTrainerDto, TrainerViewDto.class);
                EntityModel<TrainerViewDto> entityModel = EntityModel.of(trainerViewDto);

                entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                                .methodOn(TrainerController.class)
                                .updateTrainer(trainerDto))
                        .withSelfRel());

                return ResponseEntity.ok(entityModel);
            }
            throw new IllegalArgumentException("Invalid username");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/not-assigned-on-trainee/{username}")
    public ResponseEntity<?> getNotAssignedOnTrainee(@PathVariable("username") String username) {
        Collection<TrainerDto> trainers = trainerService.findTrainersNotAssignedToTrainee(username);
        if (!trainers.isEmpty()) {
            Collection<TrainerListViewDto> trainerListViewDtos = trainers.stream().map(trainer -> conversionService.convert(trainer, TrainerListViewDto.class)).collect(Collectors.toList());
            EntityModel<Collection<TrainerListViewDto>> entityModel = EntityModel.of(trainerListViewDtos);

            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                            .methodOn(TrainerController.class)
                            .getNotAssignedOnTrainee(username))
                    .withSelfRel());

            return ResponseEntity.ok(entityModel);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/trainer/{username}/trainings")
    public ResponseEntity<?> getTrainings(@PathVariable("username") String username, @RequestParam(required = false) LocalDateTime periodFrom,
                                          @RequestParam(required = false) LocalDateTime periodTo,
                                          @RequestParam(required = false) String traineeUsername) {

        if (trainerService.findByUsername(username).isPresent()) {

            Collection<TrainingListViewDto> trainingListViewDtos = trainingService.findByTrainer(username, periodFrom, periodTo, traineeUsername).stream()
                    .map(trainingDto -> TrainingListViewDto.builder()
                            .trainerUsername(username)
                            .traineeUsername(trainingDto.getTraineeDto().getUsername())
                            .trainingDateTime(trainingDto.getTrainingDate())
                            .trainingDuration(trainingDto.getTrainingDuration())
                            .trainingName(trainingDto.getTrainingName())
                            .trainingType(trainingDto.getTrainingType())
                            .build()).collect(Collectors.toList());

            EntityModel<Collection<TrainingListViewDto>> entityModel = EntityModel.of(trainingListViewDtos);

            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                            .methodOn(TrainerController.class)
                            .getTrainings(username, periodFrom, periodTo, traineeUsername))
                    .withSelfRel());

            return ResponseEntity.ok(entityModel);
        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/trainer/{username}/de-activate")
    public ResponseEntity<?> de_activate(@PathVariable("username") String username, @RequestParam("activate") boolean activate) {
        Optional<TrainerDto> trainerDto = trainerService.findByUsername(username);
        if (trainerDto.isPresent()) {
            if (activate) {
                trainerService.activate(trainerService.findByUsername(username).get());
            } else {
                trainerService.deactivate(trainerService.findByUsername(username).get());
            }
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
