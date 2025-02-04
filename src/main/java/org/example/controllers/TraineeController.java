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
import org.example.models.trainee.TraineeRegistrationDto;
import org.example.models.trainee.TraineeUpdateDto;
import org.example.models.training.TrainingDto;
import org.example.models.training.TrainingListToUpdateDto;
import org.example.models.training.TrainingListDto;
import org.example.models.training.TrainingViewDto;
import org.example.models.user.ChangeUserPasswordDto;
import org.example.models.user.LoginUserDto;
import org.example.models.trainee.TraineeDto;
import org.example.models.trainee.TraineeViewDto;
import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.example.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
@Tag(name = "Trainee Controller", description = "Controller for trainees' management")
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
    @Operation(summary = "Create new Trainee",
            parameters = {
                    @Parameter(name = "TraineeRegistrationDto", description = "Registration information", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginUserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> createTrainee(@RequestBody TraineeRegistrationDto traineeRegistrationDto) {
        TraineeDto traineeDto = conversionService.convert(traineeRegistrationDto, TraineeDto.class);
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

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();

        return ResponseEntity.created(location).body(entityModel);
    }

    @PutMapping("/trainee/change-password")
    @Operation(summary = "Change Trainee's password",
            parameters = {
                    @Parameter(name = "ChangeUserPasswordDto", description = "Change password information", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainee updated"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> changePassword(@RequestBody ChangeUserPasswordDto changeUserPasswordDto) {
        traineeService.changePassword(changeUserPasswordDto.getUsername(), changeUserPasswordDto.getOldPassword(), changeUserPasswordDto.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trainee/{username}")
    @Operation(summary = "Get Trainee",
            parameters = {
                    @Parameter(name = "username", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingViewDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getTrainee(@PathVariable("username") String username) {
        Optional<TraineeDto> traineeDto = traineeService.findByUsername(username);
        if (traineeDto.isPresent()) {
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

    @PutMapping("/trainee/{id}")
    @Operation(summary = "Update Trainee",
            parameters = {
                    @Parameter(name = "id", required = true),
                    @Parameter(name = "TraineeUpdateDto", description = "Updating information", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TraineeViewDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> updateTrainee(@PathVariable("id") Long id, @RequestBody TraineeUpdateDto traineeUpdateDto) {
        Optional<TraineeDto> foundTraineeDto = traineeService.findByUsername(traineeUpdateDto.getUsername());
        if (foundTraineeDto.isPresent()) {
            TraineeDto updatedTraineeDto = foundTraineeDto.get();

            updatedTraineeDto.setUsername(traineeUpdateDto.getUsername());
            updatedTraineeDto.setFirstName(traineeUpdateDto.getFirstName());
            updatedTraineeDto.setLastName(traineeUpdateDto.getLastName());

            if (traineeUpdateDto.getAddress() != null) {
                updatedTraineeDto.setAddress(traineeUpdateDto.getAddress());
            }
            if (traineeUpdateDto.getDateOfBirth() != null) {
                updatedTraineeDto.setDateOfBirth(traineeUpdateDto.getDateOfBirth());
            }

            updatedTraineeDto = traineeService.update(updatedTraineeDto);

            TraineeViewDto traineeViewDto = conversionService.convert(updatedTraineeDto, TraineeViewDto.class);

            EntityModel<TraineeViewDto> traineeEntityModel = EntityModel.of(traineeViewDto);

            traineeEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                            .methodOn(TraineeController.class)
                            .updateTrainee(id, traineeUpdateDto))
                    .withSelfRel());

            return ResponseEntity.ok(traineeEntityModel);
        }
        throw new IllegalArgumentException("Invalid username");
    }

    @DeleteMapping("/trainee/{username}")
    @Operation(summary = "delete Trainee",
            parameters = {
                    @Parameter(name = "username", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainee deleted"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> deleteTrainee(@PathVariable("username") String username) {
        Optional<TraineeDto> foundTraineeDto = traineeService.findByUsername(username);
        if (foundTraineeDto.isPresent()) {
            traineeService.delete(foundTraineeDto.get());
        }

        String selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                        .methodOn(TraineeController.class)
                        .deleteTrainee(username))
                .withSelfRel().getHref();

        return ResponseEntity.noContent().header("Link", "<" + selfLink + ">; rel=\"self\"").build();
    }

    @PutMapping("/trainee/{username}/trainings")
    @Operation(summary = "Update Trainee's trainings",
            parameters = {
                    @Parameter(name = "username", required = true),
                    @Parameter(name = "trainingListToUpdateDto", description = "Updating information", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee's trainings updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingListDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> updateTrainings(@PathVariable("username") String username, @RequestBody TrainingListToUpdateDto trainingListToUpdateDto) {
        Optional<TraineeDto> traineeDto = traineeService.findByUsername(username);
        if (traineeDto.isPresent()) {
            TraineeDto traineeDtoToUpdate = traineeDto.get();

            Collection<TrainingDto> trainingDtos = trainingListToUpdateDto.getTrainingIds().stream().map(id -> {
                Optional<TrainingDto> trainingDto = trainingService.findById(id);
                if (trainingDto.isPresent()) {
                    return trainingDto.get();
                }
                throw new IllegalArgumentException("Invalid training id");
            }).collect(Collectors.toList());

            traineeDtoToUpdate.setTrainings(trainingDtos);

            TraineeDto updatedTraineeDto = traineeService.update(traineeDtoToUpdate);

            EntityModel<TrainingListDto> entityModel = EntityModel.of(new TrainingListDto(updatedTraineeDto.getTrainings().stream()
                    .map(trainingDto -> conversionService.convert(trainingDto, TrainingViewDto.class)).collect(Collectors.toList())));

            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                            .methodOn(TraineeController.class)
                            .updateTrainings(username, trainingListToUpdateDto))
                    .withSelfRel());
            return ResponseEntity.ok(entityModel);
        }
        throw new IllegalArgumentException("Invalid username");
    }

    @GetMapping("/trainee/{username}/trainings")
    @Operation(summary = "Get Trainee's trainings",
            parameters = {
                    @Parameter(name = "username", required = true),
                    @Parameter(name = "periodFrom"),
                    @Parameter(name = "periodTo"),
                    @Parameter(name = "trainerUsername"),
                    @Parameter(name = "trainingType")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee's trainings found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingListDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getTrainings(@PathVariable("username") String username,
                                          @RequestParam(required = false, name = "periodFrom") LocalDateTime periodFrom,
                                          @RequestParam(required = false, name = "periodTo") LocalDateTime periodTo,
                                          @RequestParam(required = false, name = "trainerUsername") String trainerUsername,
                                          @RequestParam(required = false, name = "trainingType") TrainingType trainingType) {

        if (traineeService.findByUsername(username).isPresent()) {
            Collection<TrainingViewDto> trainingViewDtos = trainingService.findByTrainee(username, periodFrom, periodTo, trainerUsername, trainingType).stream()
                    .map(trainingDto -> conversionService.convert(trainingDto, TrainingViewDto.class)).collect(Collectors.toList());

            EntityModel<TrainingListDto> entityModel = EntityModel.of(new TrainingListDto(trainingViewDtos));

            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                            .methodOn(TraineeController.class)
                            .getTrainings(username, periodFrom, periodTo, trainerUsername, trainingType))
                    .withSelfRel());

            return ResponseEntity.ok(entityModel);
        }
        throw new IllegalArgumentException("Invalid username");
    }

    @PatchMapping("/trainee/{username}/activate")
    @Operation(summary = "De-activate Trainee",
            parameters = {
                    @Parameter(name = "username", required = true),
                    @Parameter(name = "activate", required = true),
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainee active was changed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginUserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> de_activate(@PathVariable("username") String username, @RequestParam("activate") boolean activate) {
        Optional<TraineeDto> traineeDto = traineeService.findByUsername(username);
        if (traineeDto.isPresent()) {
            if (activate) {
                traineeService.activate(traineeService.findByUsername(username).get());
            } else {
                traineeService.deactivate(traineeService.findByUsername(username).get());
            }
            return ResponseEntity.noContent().build();
        }
        throw new IllegalArgumentException("Invalid username");
    }
}
