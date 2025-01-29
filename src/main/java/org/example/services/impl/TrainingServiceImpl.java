package org.example.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.models.trainee.TraineeDto;
import org.example.models.trainer.TrainerDto;
import org.example.models.training.TrainingDto;
import org.example.repositories.entities.Training;
import org.example.repositories.TrainingDao;
import org.example.enums.TrainingType;
import org.example.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    @Qualifier("trainingDaoImpl")
    private TrainingDao trainingDao;

    @Autowired
    private ConversionService conversionService;

    @Override
    public TrainingDto add(TrainingDto entityDto) {
        Training entity = conversionService.convert(entityDto, Training.class);
        log.info("Request to save training: {}", entity.getTrainingName());

        Training savedTraining = trainingDao.save(entity);

        return savedTraining == null ? null : conversionService.convert(savedTraining, TrainingDto.class);
    }

    @Override
    public Optional<TrainingDto> findById(long id) {
        log.info("Request to find training by ID: {}", id);
        return trainingDao.findById(id).map(training -> conversionService.convert(training, TrainingDto.class));
    }

    @Override
    public Collection<TrainingDto> findAll() {
        log.info("Request to find all trainings");
        return trainingDao.findAll()
                .stream()
                .map(training -> conversionService.convert(training, TrainingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<TrainingDto> findByTrainer(String trainerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String traineeUsername) {
        log.info("Request to find training by trainer username: {}, and criteria: startDateTime: {}, endDateTime: {}, traineeUsername: {}", trainerUsername, startDateTime, endDateTime, traineeUsername);

        return trainingDao.findByTrainer(trainerUsername, startDateTime, endDateTime, traineeUsername)
                .stream()
                .map(training -> conversionService.convert(training, TrainingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<TrainingDto> findByTrainee(String traineeUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String trainerUsername, TrainingType trainingType) {
        log.info("Request to find training by trainee username: {}, and criteria: startDateTime: {}, endDateTime: {}, trainerUsername: {}, trainingType: {}", trainerUsername, startDateTime, endDateTime, traineeUsername, trainingType);

        return trainingDao.findByTrainee(traineeUsername, startDateTime, endDateTime, trainerUsername, trainingType)
                .stream()
                .map(training -> conversionService.convert(training, TrainingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TrainingDto add(Long id, TraineeDto traineeDto, TrainerDto trainerDto, TrainingType trainingType, String trainingName, Duration duration, LocalDateTime dateTime) {
        TrainingDto entityDto = TrainingDto.builder()
                .id(id)
                .traineeDto(traineeDto)
                .trainerDto(trainerDto)
                .trainingType(trainingType)
                .trainingName(trainingName)
                .trainingDuration(duration)
                .trainingDate(dateTime)
                .build();

        Training entity = conversionService.convert(entityDto, Training.class);
        log.info("Request to save training: {}", entity.getTrainingName());

        Training savedTraining = trainingDao.save(entity);

        return savedTraining == null ? null : conversionService.convert(savedTraining, TrainingDto.class);
    }
}
