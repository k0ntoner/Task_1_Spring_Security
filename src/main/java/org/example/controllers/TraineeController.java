package org.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.example.models.ChangeUserPasswordDto;
import org.example.models.TraineeDto;
import org.example.services.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/trainees")
@Slf4j
public class TraineeController {
    @Autowired
    private TraineeService traineeService;

    @PostMapping("/registration")
    public ResponseEntity<?> createTrainee(@RequestBody TraineeDto traineeDto) {
        try{
            TraineeDto savedTraineeDto = traineeService.add(traineeDto);

            EntityModel<TraineeDto> traineeEntityModel = EntityModel.of(traineeDto);

            traineeEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                    .methodOn(TraineeController.class)
                    .createTrainee(traineeDto))
                    .withSelfRel());

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("trainers/{id}").buildAndExpand(savedTraineeDto.getId()).toUri();
            return ResponseEntity.created(location).body(traineeEntityModel);
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/change-password")
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
    public ResponseEntity<?> getTrainee(@PathVariable String username) {
        Optional<TraineeDto> traineeDto=traineeService.findByUsername(username);
        if(traineeDto.isPresent()){
            EntityModel<TraineeDto> traineeEntityModel = EntityModel.of(traineeDto.get());

            traineeEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                    .methodOn(TraineeController.class)
                    .getTrainee(username))
                    .withSelfRel());

            return ResponseEntity.ok(traineeDto.get());
        }
        return ResponseEntity.badRequest().build();
    }
}
