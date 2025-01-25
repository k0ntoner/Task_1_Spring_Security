package org.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.models.ChangeUserPasswordDto;
import org.example.models.TraineeDto;
import org.example.models.TrainerDto;
import org.example.services.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.parser.Entity;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainers")
@Slf4j
public class TrainerController {
    @Autowired
    private TrainerService trainerService;



    @GetMapping
    public Collection<TrainerDto> getAllTrainers() {
        return trainerService.findAll();
    }

    @GetMapping("/trainer/{username}")
    public ResponseEntity<?> getTrainerByUsername(@PathVariable("username") String username) {
        Optional<TrainerDto> traineeDto= trainerService.findByUsername(username);

        if (traineeDto.isPresent()) {
            EntityModel<TrainerDto> traineeEntityModel = EntityModel.of(traineeDto.get());
            traineeEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                    .methodOn(TrainerController.class)
                    .getTrainerByUsername(username))
                    .withSelfRel());
            return ResponseEntity.ok(traineeEntityModel);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createTrainer(@RequestBody TrainerDto trainerDto) {
        try {
            TrainerDto savedTrainerDto = trainerService.add(trainerDto);

            EntityModel<TrainerDto> traineeEntityModel = EntityModel.of(savedTrainerDto);

            traineeEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                    .methodOn(TrainerController.class)
                    .createTrainer(trainerDto))
                    .withSelfRel());

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/trainer/{username}").buildAndExpand(savedTrainerDto.getUsername()).toUri();

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
            trainerService.changePassword(changeUserPasswordDto.getUsername(), changeUserPasswordDto.getOldPassword(), changeUserPasswordDto.getNewPassword());
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

}
