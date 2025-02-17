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
import org.example.metrics.UserRegistrationsMetric;
import org.example.models.trainer.*;
import org.example.models.training.TrainingListDto;
import org.example.models.training.TrainingViewDto;
import org.example.models.user.ChangeUserPasswordDto;
import org.example.models.user.AuthUserDto;
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
@Tag(name = "Trainer Controller", description = "Controller for trainers' management")
public class TrainerController {
    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private UserRegistrationsMetric userRegistrationsCounter;

    @GetMapping("/trainer/{username}")
    @Operation(summary = "Get Trainer",
            parameters = {
                    @Parameter(name = "username", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainerViewDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getTrainerByUsername(@PathVariable("username") String username) {
        return  ResponseEntity.ok(trainerService.findByUsername(username));
    }

    @PostMapping("/registration")
    @Operation(summary = "Create new Trainer",
            parameters = {
                    @Parameter(name = "TrainerRegistrationDto", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthUserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> createTrainer(@RequestBody @Valid TrainerRegistrationDto trainerRegistrationDto) {
        TrainerDto savedTrainerDto = trainerService.add(conversionService.convert(trainerRegistrationDto, TrainerDto.class));
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath().path("/trainer/{username}").buildAndExpand(savedTrainerDto.getUsername()).toUri())
                .body(conversionService.convert(savedTrainerDto, AuthUserDto.class));
    }

    @PutMapping("/trainer/change-password")
    @Operation(summary = "Change Trainer's password",
            parameters = {
                    @Parameter(name = "changeUserPasswordDto", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainer's password changed"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> changePassword(@RequestBody @Valid  ChangeUserPasswordDto changeUserPasswordDto) {
        trainerService.changePassword(changeUserPasswordDto.getUsername(), changeUserPasswordDto.getOldPassword(), changeUserPasswordDto.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/trainer/{username}")
    @Operation(summary = "Update Trainer",
            parameters = {
                    @Parameter(name = "username", required = true),
                    @Parameter(name = "trainerDto", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingViewDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> updateTrainer(@PathVariable("username") String username, @RequestBody @Valid  TrainerUpdateDto trainerUpdateDto) {
            return ResponseEntity.ok(trainerService.update(username, trainerUpdateDto));
    }

    @GetMapping("/not-assigned-on-trainee/{username}")
    @Operation(summary = "Get Trainers not assigned on trainee",
            parameters = {
                    @Parameter(name = "username", required = true),
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainers found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainerListDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getNotAssignedOnTrainee(@PathVariable("username") String username) {
        return ResponseEntity.ok(trainerService.findTrainersNotAssignedToTrainee(username));
    }

    @GetMapping("/trainer/{username}/trainings")
    @Operation(summary = "Get Trainer's list of trainings",
            parameters = {
                    @Parameter(name = "username", required = true),
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingListDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getTrainings(@PathVariable("username") String username, @RequestParam(required = false, name = "periodFrom") LocalDateTime periodFrom,
                                          @RequestParam(required = false, name = "periodTo") LocalDateTime periodTo,
                                          @RequestParam(required = false, name = "traineeUsername") String traineeUsername) {

        return ResponseEntity.ok(trainingService.findByTrainer(username, periodFrom, periodTo, traineeUsername));
    }

    @PatchMapping("/trainer/{username}/activate")
    @Operation(summary = "De-activate Trainer",
            parameters = {
                    @Parameter(name = "username", required = true),
                    @Parameter(name = "activate", required = true),
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainer active was changed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingListDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> de_activate(@PathVariable("username") String username, @RequestParam("activate") boolean activate) {
        if (activate) {
            trainerService.activate(trainerService.findByUsername(username));
        } else {
            trainerService.deactivate(trainerService.findByUsername(username));
        }
        return ResponseEntity.noContent().build();
    }

}
