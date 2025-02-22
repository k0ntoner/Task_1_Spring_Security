package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.TrainingType;
import org.example.metrics.UserRegistrationsMetric;
import org.example.models.trainee.TraineeRegistrationDto;
import org.example.models.trainee.TraineeUpdateDto;
import org.example.models.training.TrainingDto;
import org.example.models.training.TrainingListToUpdateDto;
import org.example.models.training.TrainingListDto;
import org.example.models.training.TrainingViewDto;
import org.example.models.user.ChangeUserPasswordDto;
import org.example.models.user.AuthUserDto;
import org.example.models.trainee.TraineeDto;
import org.example.models.trainee.TraineeViewDto;
import org.example.services.TraineeService;
import org.example.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    private UserRegistrationsMetric userRegistrationsCounter;

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
                            schema = @Schema(implementation = AuthUserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> createTrainee(@RequestBody @Valid TraineeRegistrationDto traineeRegistrationDto) {

        TraineeDto savedTraineeDto = traineeService.add(conversionService.convert(traineeRegistrationDto, TraineeDto.class));

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri())
                .body(conversionService.convert(savedTraineeDto, AuthUserDto.class));
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
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangeUserPasswordDto changeUserPasswordDto) {
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
        return ResponseEntity.ok(traineeService.findByUsername(username));
    }

    @PutMapping("/trainee/{username}")
    @Operation(summary = "Update Trainee",
            parameters = {
                    @Parameter(name = "username", required = true),
                    @Parameter(name = "TraineeUpdateDto", description = "Updating information", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TraineeViewDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> updateTrainee(@PathVariable("username") String username, @RequestBody @Valid TraineeUpdateDto traineeUpdateDto) {
        return ResponseEntity.ok(traineeService.update(username, traineeUpdateDto));
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
        traineeService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<?> updateTrainings(@PathVariable("username") String username, @RequestBody @Valid TrainingListToUpdateDto trainingListToUpdateDto) {
        return ResponseEntity.ok(traineeService.updateTraineeTrainings(username, trainingListToUpdateDto));
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

        Collection<TrainingViewDto> trainingViewDtos = trainingService.findByTrainee(username, periodFrom, periodTo, trainerUsername, trainingType).stream()
                .map(trainingDto -> conversionService.convert(trainingDto, TrainingViewDto.class)).collect(Collectors.toList());

        return ResponseEntity.ok(new TrainingListDto(trainingViewDtos));
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
                            schema = @Schema(implementation = AuthUserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> de_activate(@PathVariable("username") String username, @RequestParam("activate") boolean activate) {
        if (activate) {
            traineeService.activate(traineeService.findByUsername(username));
        } else {
            traineeService.deactivate(traineeService.findByUsername(username));
        }
        return ResponseEntity.noContent().build();
    }
}
