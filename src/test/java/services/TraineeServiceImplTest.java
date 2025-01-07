package services;

import org.example.models.Trainee;
import org.example.repositories.TraineeDaoImpl;
import org.example.repositories.UserDao;
import org.example.services.TraineeServiceImpl;
import org.example.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


public class TraineeServiceImplTest {
    private UserDao<Trainee> traineeMockDao;
    private UserService<Trainee> traineeService;

    @BeforeEach
    public void setUp() {
        traineeMockDao = Mockito.mock(TraineeDaoImpl.class);
        traineeService = new TraineeServiceImpl(traineeMockDao);
    }

    public Trainee buildTraineeForAdding(long id) {
        return Trainee.builder()
                .firstName("firstName" + id)
                .lastName("lastName" + id)
                .isActive(false)
                .dateOfBirth(LocalDate.of(2024, 12, 12))
                .address("address" + id)
                .build();
    }

    public Trainee buildFullTrainee(long id) {
        return Trainee.builder()
                .userId(id)
                .firstName("fullFirstName" + id)
                .lastName("fullLastName" + id)
                .username("fullUsername" + id)
                .password("fullPassword" + id)
                .isActive(false)
                .dateOfBirth(LocalDate.of(2024, 12, 12))
                .address("address" + id)
                .build();
    }

    @Test
    public void testAddingTrainee() {
        Trainee trainee = buildFullTrainee(1L);
        when(traineeMockDao.add(trainee)).thenReturn(trainee);
        traineeService.add(trainee);

        Trainee checkTrainee = traineeService.add(trainee);
        assertNotEquals(0, checkTrainee.getUserId());
        assertEquals(trainee.getUserId(), checkTrainee.getUserId());
        assertEquals(trainee.getFirstName(), checkTrainee.getFirstName());
        assertEquals(trainee.getLastName(), checkTrainee.getLastName());
        assertEquals(trainee.getUsername(), checkTrainee.getUsername());
        assertEquals(trainee.getPassword(), checkTrainee.getPassword());
        assertEquals(trainee.getAddress(), checkTrainee.getAddress());
        assertEquals(trainee.isActive(), checkTrainee.isActive());
    }

    @Test
    public void testUpdatingTrainee() {
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newUsername = "newUsername";
        String newPassword = "newPassword";
        String newAddress = "newAddress";
        boolean newActive = true;
        LocalDate newDateOfBirth = LocalDate.of(2024, 12, 16);

        Trainee trainee = buildFullTrainee(1L);

        when(traineeMockDao.update(trainee)).thenReturn(trainee);
        when(traineeMockDao.add(trainee)).thenReturn(trainee);
        when(traineeMockDao.findById(trainee.getUserId())).thenReturn(trainee);

        traineeService.add(trainee);

        trainee.setFirstName(newFirstName);
        trainee.setLastName(newLastName);
        trainee.setUsername(newUsername);
        trainee.setPassword(newPassword);
        trainee.setAddress(newAddress);
        trainee.setActive(newActive);
        trainee.setDateOfBirth(newDateOfBirth);

        Trainee checkTrainee = traineeService.update(trainee);

        assertNotEquals(0, checkTrainee.getUserId());
        assertEquals(newFirstName, checkTrainee.getFirstName());
        assertEquals(newLastName, checkTrainee.getLastName());
        assertEquals(newUsername, checkTrainee.getUsername());
        assertEquals(newPassword, checkTrainee.getPassword());
        assertEquals(newAddress, checkTrainee.getAddress());
        assertEquals(newActive, checkTrainee.isActive());
        assertEquals(newDateOfBirth, checkTrainee.getDateOfBirth());
    }

    @Test
    public void testDeletingTrainee() {
        Trainee trainee = buildFullTrainee(1L);
        when(traineeMockDao.delete(trainee)).thenReturn(true);
        when(traineeMockDao.findById(trainee.getUserId())).thenReturn(trainee);
        assertTrue(traineeService.delete(trainee));

    }

    @Test
    void testFindAllTrainee() {
        Trainee trainee = buildFullTrainee(1L);
        Trainee secondTrainee = buildFullTrainee(2L);

        when(traineeMockDao.findAll()).thenReturn(List.of(trainee, secondTrainee));
        Collection<Trainee> traineeList = traineeService.findAll();
        assertEquals(2, traineeList.size());
        List<Trainee> trainees = traineeList.stream().toList();

        assertAll(
                () -> assertEquals("fullFirstName1", trainees.get(0).getFirstName()),
                () -> assertEquals("fullLastName1", trainees.get(0).getLastName()),
                () -> assertEquals("fullFirstName2", trainees.get(1).getFirstName()),
                () -> assertEquals("fullLastName2", trainees.get(1).getLastName())
        );
        traineeList.forEach(t -> {
            assertNotEquals(0, t.getUserId());
            assertNotNull(t.getFirstName());
            assertNotNull(t.getLastName());
            assertNotNull(t.getUsername());
            assertNotNull(t.getPassword());
            assertNotNull(t.getAddress());
            assertFalse(t.isActive());
        });
    }

    @Test
    void testUpdateNotExistingTrainee() {
        Trainee trainee = buildFullTrainee(1L);
        when(traineeMockDao.findById(1L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> {
            traineeService.update(trainee);
        });
    }

    @Test
    void testDeleteNotExistingTrainee() {
        Trainee trainee = buildFullTrainee(1L);
        when(traineeMockDao.delete(trainee)).thenReturn(false);
        assertFalse(traineeService.delete(trainee));
    }
}