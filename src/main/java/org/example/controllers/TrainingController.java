package org.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.TrainingType;
import org.example.models.trainer.TrainerDto;
import org.example.models.trainee.TraineeDto;
import org.example.models.training.TrainingDto;
import org.example.models.training.TrainingAddDto;
import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.example.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainings")
@Slf4j
public class TrainingController {
    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private ConversionService conversionService;

    @PostMapping
    public ResponseEntity<?> createTraining(@RequestBody TrainingAddDto trainingAddDto) {
        try {
            Optional<TraineeDto> foundTraineeDto = traineeService.findByUsername(trainingAddDto.getTraineeUsername());
            if (foundTraineeDto.isEmpty()) {
                throw new IllegalArgumentException("Trainee  not found");
            }

            trainingAddDto.setTraineeDto(foundTraineeDto.get());

            Optional<TrainerDto> foundTrainerDto = trainerService.findByUsername(trainingAddDto.getTrainerUsername());
            if (foundTrainerDto.isEmpty()) {
                throw new IllegalArgumentException("Trainer not found");
            }

            trainingAddDto.setTrainerDto(foundTrainerDto.get());

            TrainingDto trainingDto = conversionService.convert(trainingAddDto, TrainingDto.class);
            TrainingDto savedTrainingDto = trainingService.add(trainingDto);

            EntityModel<TrainingDto> entityModel = EntityModel.of(savedTrainingDto);
            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TrainingController.class).createTraining(trainingAddDto)).withSelfRel());

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/trainings/{id}").buildAndExpand(savedTrainingDto.getId()).toUri();

            return ResponseEntity.created(location).body(entityModel);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/types")
    public ResponseEntity<?> getTrainingTypes() {
        CollectionModel<TrainingType> collectionModel = CollectionModel.of(List.of(TrainingType.values()));

        collectionModel.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(TrainingController.class)
                        .getTrainingTypes()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}
