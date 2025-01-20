package services;


import org.example.models.TraineeDto;
import org.example.repositories.TraineeDao;
import org.example.repositories.entities.Trainee;
import org.example.services.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class TraineeServiceImplTest {
    @Mock
    private TraineeDao traineeDao;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private TraineeDto testTraineeDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTraineeDto = buildTraineeDto();
        doAnswer(invocation -> {
            Trainee trainee = invocation.getArgument(0);
            trainee.setId(1L);
            return trainee;
        }).when(traineeDao).save(any(Trainee.class));

        doAnswer(invocation -> {
            Trainee trainee = invocation.getArgument(0);
            return TraineeDto.builder()
                    .id(trainee.getId())
                    .firstName(trainee.getFirstName())
                    .lastName(trainee.getLastName())
                    .password(trainee.getPassword())
                    .dateOfBirth(trainee.getDateOfBirth())
                    .address(trainee.getAddress())
                    .isActive(true)
                    .build();
        }).when(conversionService).convert(any(Trainee.class), eq(TraineeDto.class));

        doAnswer(invocation -> {
            TraineeDto traineeDto = invocation.getArgument(0);
            return Trainee.builder()
                    .id(traineeDto.getId())
                    .firstName(traineeDto.getFirstName())
                    .lastName(traineeDto.getLastName())
                    .password(traineeDto.getPassword())
                    .dateOfBirth(traineeDto.getDateOfBirth())
                    .address(traineeDto.getAddress())
                    .isActive(true)
                    .build();
        }).when(conversionService).convert(any(TraineeDto.class), eq(Trainee.class));

    }

    public TraineeDto buildTraineeDto() {
        return TraineeDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .password("JohnDoe")
                .dateOfBirth(LocalDate.of(1998, 11, 23))
                .address("Test Street")
                .isActive(true)
                .build();
    }

    public Trainee buildTrainee() {
        return Trainee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .password("JohnDoe")
                .dateOfBirth(LocalDate.of(1998, 11, 23))
                .address("Test Street")
                .isActive(true)
                .build();
    }

    public Trainee buildTrainee(TraineeDto traineeDto) {
        return Trainee.builder()
                .id(traineeDto.getId())
                .firstName(traineeDto.getFirstName())
                .lastName(traineeDto.getLastName())
                .password(traineeDto.getPassword())
                .dateOfBirth(traineeDto.getDateOfBirth())
                .address(traineeDto.getAddress())
                .isActive(traineeDto.isActive())
                .build();
    }


    @Test
    @DisplayName("Should return saved Trainee")
    public void save_ShouldReturnSavedTrainee() {
        TraineeDto newTraineeDto = traineeService.add(testTraineeDto);

        verify(traineeDao, times(1)).save(any(Trainee.class));
    }

    @Test
    @DisplayName("Should return updated Trainee")
    public void update_ShouldUpdateTrainee() {
        TraineeDto newTraineeDto = traineeService.add(testTraineeDto);

        when(traineeDao.update(any(Trainee.class))).thenReturn(buildTrainee(newTraineeDto));
        when(traineeDao.findById(newTraineeDto.getId())).thenReturn(Optional.of(buildTrainee(newTraineeDto)));

        TraineeDto updatedTraineeDto = traineeService.update(newTraineeDto);

        verify(traineeDao, times(1)).update(any(Trainee.class));

    }

    @Test
    @DisplayName("Should delete Trainee")
    public void delete_ShouldDeleteTrainee() {
        testTraineeDto = traineeService.add(testTraineeDto);

        traineeService.delete(testTraineeDto);

        verify(traineeDao, times(1)).delete(any(Trainee.class));
    }

    @Test
    @DisplayName("Should delete Trainee by username")
    public void delete_ShouldDeleteTraineeByUsername() {
        testTraineeDto = traineeService.add(testTraineeDto);

        when(traineeDao.findByUsername(testTraineeDto.getUsername())).thenReturn(Optional.of(buildTrainee(testTraineeDto)));

        traineeService.deleteByUsername(testTraineeDto.getUsername());

        verify(traineeDao, times(1)).delete(any(Trainee.class));
    }

    @Test
    @DisplayName("Should find Trainee by id")
    public void findById_ShouldFindTraineeById() {
        testTraineeDto = traineeService.add(testTraineeDto);
        when(traineeDao.findById(1L)).thenReturn(Optional.of(buildTrainee(testTraineeDto)));

        TraineeDto foundTraineeDto = traineeService.findById(testTraineeDto.getId()).get();

        verify(traineeDao, times(1)).findById(testTraineeDto.getId());
    }

    @Test
    @DisplayName("Should find Collection of all Trainees")
    public void findAll_ShouldFindAllTrainees() {

        testTraineeDto = traineeService.add(testTraineeDto);
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(buildTrainee());
        when(traineeDao.findAll()).thenReturn(trainees);
        Collection<Trainee> foundTrainees = traineeDao.findAll();

        assertTrue(trainees.size() > 0);
        verify(traineeDao, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find Trainee by username")
    public void findByUsername_ShouldFindTraineeByUserName() {
        testTraineeDto = traineeService.add(testTraineeDto);

        when(traineeDao.findByUsername(testTraineeDto.getUsername())).thenReturn(Optional.of(buildTrainee(testTraineeDto)));

        TraineeDto foundTraineeDto = traineeService.findByUsername(testTraineeDto.getUsername()).get();

        verify(traineeDao, times(1)).findByUsername(any());
    }

    @Test
    @DisplayName("Should change trainee password")
    public void changePassword_ShouldChangeTraineePassword() {
        testTraineeDto = traineeService.add(testTraineeDto);
        when(traineeDao.findByUsername(testTraineeDto.getUsername())).thenReturn(Optional.of(buildTrainee(testTraineeDto)));
        when(traineeDao.update(any(Trainee.class))).thenReturn(buildTrainee());

        traineeService.changePassword(testTraineeDto.getUsername(), "JohnDoe", "newPass");

        verify(traineeDao, times(1)).update(any(Trainee.class));
    }

    @Test
    @DisplayName("Should activate Trainee")
    public void activate_ShouldActivateTrainee() {
        testTraineeDto = traineeService.add(testTraineeDto);

        when(traineeDao.update(any(Trainee.class))).thenReturn(buildTrainee(testTraineeDto));
        traineeService.activate(testTraineeDto);
        verify(traineeDao, times(1)).update(any(Trainee.class));
    }

    @Test
    @DisplayName("Should deactivate Trainee")
    public void activate_ShouldDeactivateTrainee() {
        testTraineeDto = traineeService.add(testTraineeDto);

        when(traineeDao.update(any(Trainee.class))).thenReturn(buildTrainee(testTraineeDto));
        traineeService.deactivate(testTraineeDto);
        verify(traineeDao, times(1)).update(any(Trainee.class));
    }

}