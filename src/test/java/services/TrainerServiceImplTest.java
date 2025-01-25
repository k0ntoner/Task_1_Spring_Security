package services;


import org.example.models.TrainerDto;
import org.example.repositories.TrainerDao;
import org.example.repositories.entities.Trainer;
import org.example.enums.TrainingType;
import org.example.services.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TrainerServiceImplTest {
    @Mock
    private TrainerDao trainerDao;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private TrainerDto testTrainerDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTrainerDto = buildTrainerDto();
        doAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            trainer.setId(1L);
            return trainer;
        }).when(trainerDao).save(any(Trainer.class));

        doAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            return TrainerDto.builder()
                    .id(trainer.getId())
                    .firstName(trainer.getFirstName())
                    .lastName(trainer.getLastName())
                    .password(trainer.getPassword())
                    .specialization(trainer.getSpecialization())
                    .isActive(true)
                    .build();
        }).when(conversionService).convert(any(Trainer.class), eq(TrainerDto.class));

        doAnswer(invocation -> {
            TrainerDto trainerDto = invocation.getArgument(0);
            return Trainer.builder()
                    .id(trainerDto.getId())
                    .firstName(trainerDto.getFirstName())
                    .lastName(trainerDto.getLastName())
                    .password(trainerDto.getPassword())
                    .specialization(trainerDto.getSpecialization())
                    .isActive(true)
                    .build();
        }).when(conversionService).convert(any(TrainerDto.class), eq(Trainer.class));

    }

    public TrainerDto buildTrainerDto() {
        return TrainerDto.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Tyson")
                .password("MikeTyson")
                .specialization(TrainingType.STRENGTH)
                .isActive(true)
                .build();
    }

    public Trainer buildTrainer() {
        return Trainer.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Tyson")
                .password("MikeTyson")
                .specialization(TrainingType.STRENGTH)
                .isActive(true)
                .build();
    }

    public Trainer buildTrainer(TrainerDto trainerDto) {
        return Trainer.builder()
                .id(trainerDto.getId())
                .firstName(trainerDto.getFirstName())
                .lastName(trainerDto.getLastName())
                .password(trainerDto.getPassword())
                .specialization(trainerDto.getSpecialization())
                .isActive(trainerDto.isActive())
                .build();
    }

    @Test
    @DisplayName("Should return saved Trainer")
    public void save_ShouldReturnSavedTrainer() {
        TrainerDto newTrainerDto = trainerService.add(testTrainerDto);

        verify(trainerDao, times(1)).save(any(Trainer.class));
    }

    @Test
    @DisplayName("Should return updated Trainer")
    public void update_ShouldUpdateTrainer() {
        TrainerDto newTrainerDto = trainerService.add(testTrainerDto);

        when(trainerDao.update(any(Trainer.class))).thenReturn(buildTrainer(newTrainerDto));

        TrainerDto updatedTrainerDto = trainerService.update(newTrainerDto);

        verify(trainerDao, times(1)).update(any(Trainer.class));
    }

    @Test
    @DisplayName("Should find Trainer by id")
    public void findById_ShouldFindTrainerById() {
        testTrainerDto = trainerService.add(testTrainerDto);
        when(trainerDao.findById(1L)).thenReturn(Optional.of(buildTrainer(testTrainerDto)));

        TrainerDto foundTrainerDto = trainerService.findById(1L).get();

        verify(trainerDao, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should find Collection of all Trainers")
    public void findAll_ShouldFindAllTrainers() {
        testTrainerDto = trainerService.add(testTrainerDto);
        List<Trainer> trainers = new ArrayList<>();

        trainers.add(buildTrainer());

        when(trainerDao.findAll()).thenReturn(trainers);

        Collection<TrainerDto> foundTrainers = trainerService.findAll();

        assertTrue(foundTrainers.size() > 0);
        verify(trainerDao, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find Trainer by username")
    public void findByUsername_ShouldFindTrainerByUserName() {
        testTrainerDto = trainerService.add(testTrainerDto);

        when(trainerDao.findByUsername(testTrainerDto.getUsername())).thenReturn(Optional.of(buildTrainer(testTrainerDto)));

        TrainerDto foundTrainerDto = trainerService.findByUsername(testTrainerDto.getUsername()).get();

        verify(trainerDao, times(1)).findByUsername(testTrainerDto.getUsername());
    }

    @Test
    @DisplayName("Should change trainer password")
    public void changePassword_ShouldChangeTrainerPassword() {
        testTrainerDto = trainerService.add(testTrainerDto);
        when(trainerDao.findByUsername(testTrainerDto.getUsername())).thenReturn(Optional.of(buildTrainer(testTrainerDto)));
        when(trainerDao.update(any(Trainer.class))).thenReturn(buildTrainer());

        trainerService.changePassword(testTrainerDto.getUsername(), "MikeTyson", "newPass");
        verify(trainerDao, times(1)).update(any(Trainer.class));
    }

    @Test
    @DisplayName("Should activate Trainer")
    public void activate_ShouldActivateTrainer() {
        testTrainerDto = trainerService.add(testTrainerDto);
        when(trainerDao.update(any(Trainer.class))).thenReturn(buildTrainer(testTrainerDto));
        trainerService.activate(testTrainerDto);

        verify(trainerDao, times(1)).update(any(Trainer.class));
    }

    @Test
    @DisplayName("Should deactivate Trainer")
    public void activate_ShouldDeactivateTrainer() {
        testTrainerDto = trainerService.add(testTrainerDto);

        when(trainerDao.update(any(Trainer.class))).thenReturn(buildTrainer(testTrainerDto));

        trainerService.deactivate(testTrainerDto);

        verify(trainerDao, times(1)).update(any(Trainer.class));
    }

}
