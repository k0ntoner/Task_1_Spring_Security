package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.example.models.trainer.*;
import org.example.models.training.TrainingListDto;
import org.example.models.training.TrainingViewDto;
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
    @Operation(summary = "Create new Trainer",
            parameters = {
                    @Parameter(name = "TrainerRegistrationDto", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginUserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
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
    @Operation(summary = "Change Trainer's password",
            parameters = {
                    @Parameter(name = "changeUserPasswordDto", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainer's password changed"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> changePassword(@RequestBody ChangeUserPasswordDto changeUserPasswordDto) {
        try {
            trainerService.changePassword(changeUserPasswordDto.getUsername(), changeUserPasswordDto.getOldPassword(), changeUserPasswordDto.getNewPassword());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/trainer/{id}")
    @Operation(summary = "Update Trainer",
            parameters = {
                    @Parameter(name = "id", required = true),
                    @Parameter(name = "trainerDto", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingViewDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> updateTrainer(@PathVariable("id") Long id, @RequestBody TrainerUpdateDto trainerDto) {
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
                                .updateTrainer(id, trainerDto))
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
        Collection<TrainerDto> trainers = trainerService.findTrainersNotAssignedToTrainee(username);
        if (!trainers.isEmpty()) {
            Collection<TrainerListViewDto> trainerListViewDtos = trainers.stream().map(trainer -> conversionService.convert(trainer, TrainerListViewDto.class)).collect(Collectors.toList());
            EntityModel<TrainerListDto> entityModel = EntityModel.of(new TrainerListDto(trainerListViewDtos));
            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                            .methodOn(TrainerController.class)
                            .getNotAssignedOnTrainee(username))
                    .withSelfRel());

            return ResponseEntity.ok(entityModel);
        }
        return ResponseEntity.badRequest().build();
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

        if (trainerService.findByUsername(username).isPresent()) {

            Collection<TrainingViewDto> trainingViewDtos = trainingService.findByTrainer(username, periodFrom, periodTo, traineeUsername).stream()
                    .map(trainingDto -> conversionService.convert(trainingDto, TrainingViewDto.class)).collect(Collectors.toList());

            EntityModel<TrainingListDto> entityModel = EntityModel.of(new TrainingListDto(trainingViewDtos));

            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                            .methodOn(TrainerController.class)
                            .getTrainings(username, periodFrom, periodTo, traineeUsername))
                    .withSelfRel());

            return ResponseEntity.ok(entityModel);
        }
        return ResponseEntity.badRequest().build();
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
