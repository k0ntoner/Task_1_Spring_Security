package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.TrainingType;
import org.example.models.trainer.TrainerDto;
import org.example.models.trainee.TraineeDto;
import org.example.models.training.*;
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
@Tag(name = "Training Controller", description = "Controller for trainings' management")
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
    @Operation(summary = "Create Training",
            parameters = {
                    @Parameter(name = "trainingAddDto", required = true),
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Training created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingViewDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> createTraining(@RequestBody TrainingAddDto trainingAddDto) {
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

        EntityModel<TrainingViewDto> entityModel = EntityModel.of(conversionService.convert(savedTrainingDto, TrainingViewDto.class));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TrainingController.class).createTraining(trainingAddDto)).withSelfRel());

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/trainings/{id}").buildAndExpand(savedTrainingDto.getId()).toUri();

        return ResponseEntity.created(location).body(entityModel);
    }

    @GetMapping("/types")
    @Operation(summary = "Get TrainingTypes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TrainingTypes found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingTypeListDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getTrainingTypes() {
        TrainingTypeListDto trainingTypeListDto = new TrainingTypeListDto(List.of(TrainingType.values()));
        EntityModel<TrainingTypeListDto> entityModel = EntityModel.of(trainingTypeListDto);
        return ResponseEntity.ok(entityModel);
    }
}
