package org.example.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.models.TrainingDto;
import org.example.repositories.entities.Training;
import org.example.repositories.TrainingDao;
import org.example.enums.TrainingType;
import org.example.services.TrainingService;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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

    @Override
    public TrainingDto add(TrainingDto entityDto) {
        Training entity = UserUtils.convertTrainingDtoToEntity(entityDto);
        log.info("Request to save training: {}", entity.getTrainingName());

        Training savedTraining = trainingDao.save(entity);

        return savedTraining == null ? null : UserUtils.convertTrainingEntityToDto(savedTraining);
    }

    @Override
    public Optional<TrainingDto> findById(long id) {
        log.info("Request to find training by ID: {}", id);
        return trainingDao.findById(id).map(UserUtils::convertTrainingEntityToDto);
    }

    @Override
    public Collection<TrainingDto> findAll() {
        log.info("Request to find all trainings");
        return trainingDao.findAll()
                .stream()
                .map(UserUtils::convertTrainingEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<TrainingDto> findByTrainer(String trainerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String traineeUsername) {
        log.info("Request to find training by trainer username: {}, and criteria: startDateTime: {}, endDateTime: {}, traineeUsername: {}", trainerUsername, startDateTime, endDateTime, traineeUsername);

        return trainingDao.findByTrainer(trainerUsername, startDateTime, endDateTime, traineeUsername)
                .stream()
                .map(UserUtils::convertTrainingEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<TrainingDto> findByTrainee(String traineeUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String trainerUsername, TrainingType trainingType) {
        log.info("Request to find training by trainee username: {}, and criteria: startDateTime: {}, endDateTime: {}, trainerUsername: {}, trainingType: {}", trainerUsername, startDateTime, endDateTime, traineeUsername, trainingType);

        return trainingDao.findByTrainee(traineeUsername, startDateTime, endDateTime, trainerUsername, trainingType)
                .stream()
                .map(UserUtils::convertTrainingEntityToDto)
                .collect(Collectors.toList());
    }
}
