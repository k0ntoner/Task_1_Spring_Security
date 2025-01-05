package repositories;

import configs.ConfigTest;
import org.example.models.Trainee;
import org.example.repositories.UserDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeDaoTest {
    AnnotationConfigApplicationContext context;
    UserDao<Trainee> traineeDAO;

    @BeforeEach
    public void setUp() {
        context = new AnnotationConfigApplicationContext(ConfigTest.class);
        traineeDAO = (UserDao<Trainee>) context.getBean("traineeDaoImpl");
    }
    @AfterEach
    public void tearDown() {
        context.close();
    }
    public Trainee buildTraineeForAdding(long id) {
        return Trainee.builder()
                .firstName("firstName"+id)
                .lastName("lastName"+id)
                .isActive(false).
                dateOfBirth(LocalDate.of(2024, 12, 12))
                .address("address"+id)
                .build();
    }
    public Trainee buildFullTrainee(long id) {
        return Trainee.builder()
                .userId(id)
                .firstName("fullFirstName"+id)
                .lastName("fullLastName"+id)
                .username("fullUsername"+id)
                .password("fullPassword"+id)
                .isActive(true)
                .dateOfBirth(LocalDate.of(2024, 12, 12))
                .address("address"+id)
                .build();
    }
    @Test
    public void testAddingTrainee() {
        Trainee trainee= buildTraineeForAdding(1L);

        Trainee checkTrainee = traineeDAO.add(trainee);

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
            Trainee trainee=traineeDAO.add(buildTraineeForAdding(1L));

            Trainee secondTrainee = buildTraineeForAdding(1L);
            Trainee checkTrainee = traineeDAO.update(secondTrainee);

            assertEquals(secondTrainee.getUserId(), checkTrainee.getUserId());
            assertEquals(secondTrainee.getFirstName(), checkTrainee.getFirstName());
            assertEquals(secondTrainee.getLastName(), checkTrainee.getLastName());
            assertEquals(secondTrainee.getUsername(), checkTrainee.getUsername());
            assertEquals(secondTrainee.getPassword(), checkTrainee.getPassword());
            assertEquals(secondTrainee.getAddress(), checkTrainee.getAddress());
            assertEquals(secondTrainee.isActive(), checkTrainee.isActive());
    }
    @Test
    public void testDeletingTrainee() {
        Trainee checkTrainee = traineeDAO.add(buildTraineeForAdding(1L));
        traineeDAO.delete(checkTrainee);
        assertNull(traineeDAO.findById(checkTrainee.getUserId()));

    }
    @Test void testFindAllTrainee() {

        Trainee trainee=traineeDAO.add(buildTraineeForAdding(1L));

        Trainee secondTrainee=traineeDAO.add(buildTraineeForAdding(2L));
        Collection<Trainee> traineeList = traineeDAO.findAll();
        assertEquals(2, traineeList.size());
        List<Trainee> trainees = traineeList.stream().toList();

        assertAll(
                () -> assertEquals("firstName1", trainees.get(0).getFirstName()),
                () -> assertEquals("lastName1", trainees.get(0).getLastName()),
                () -> assertEquals("firstName2", trainees.get(1).getFirstName()),
                () -> assertEquals("lastName2", trainees.get(1).getLastName())
        );
        traineeList.forEach(t -> {
            assertNotEquals(0,t.getUserId());
            assertNotNull(t.getFirstName());
            assertNotNull(t.getLastName());
            assertNotNull(t.getAddress());
            assertFalse(t.isActive());
        });
    }
    @Test void testUpdateNotExistingTrainee() {
        //Will not throw an Exception because this logic located in Service class.
        Trainee trainee = buildFullTrainee(1L);
        assertDoesNotThrow(() -> {traineeDAO.update(trainee);});


    }
    @Test void testDeleteNotExistingTrainee() {
        //Will not throw an Exception because this logic located in Service class.

        Trainee trainee = buildFullTrainee(1L);
        assertDoesNotThrow(() -> {traineeDAO.delete(trainee);});
    }
}
