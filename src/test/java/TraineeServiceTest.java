
import org.example.configs.ConfigTest;
import org.example.loaders.Loader;
import org.example.loaders.TraineeLoader;
import org.example.models.Trainee;
import org.example.repositories.TraineeDAOImpl;
import org.example.repositories.UserDAO;
import org.example.services.TraineeServiceImpl;
import org.example.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;


public class TraineeServiceTest {
    private UserDAO<Trainee> traineeMockDAO;
    private UserService<Trainee> traineeService;
    @BeforeEach
    public void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        Loader<Trainee> traineeLoader=context.getBean(TraineeLoader.class);
        traineeLoader.clear();
        context.close();
        traineeMockDAO = Mockito.mock(TraineeDAOImpl.class);
        traineeService = new TraineeServiceImpl(traineeMockDAO);

    }
    @Test
    public void testAddingTrainee() {


        Trainee trainee = Trainee.builder()
                .userId(1L)
                .firstName("firstName")
                .lastName("lastName")
                .username("username")
                .password("password")
                .isActive(false).
                dateOfBirth(LocalDate.of(2024, 12, 12))
                .address("address")
                .build();
        when(traineeMockDAO.findById(1L)).thenReturn(trainee);
        traineeService.add(trainee);

        Trainee checkTrainee = traineeService.findById(trainee.getUserId());
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

        Trainee trainee = Trainee.builder()
                .userId(1L)
                .firstName("firstName1")
                .lastName("lastName1")
                .username("username1")
                .password("password1")
                .isActive(false).
                dateOfBirth(LocalDate.of(2024, 12, 12))
                .address("address1")
                .build();
        traineeService.add(trainee);

        Trainee secondTrainee = Trainee.builder()
                .userId(1)
                .firstName("firstName2")
                .lastName("lastName2")
                .username("username2")
                .password("!fo82fef41")
                .isActive(false).
                dateOfBirth(LocalDate.of(2024, 12, 12))
                .address("address2")
                .build();
        traineeService.update(1, secondTrainee);
        when(traineeMockDAO.findById(1L)).thenReturn(secondTrainee);
        Trainee checkTrainee = traineeService.findById(secondTrainee.getUserId());
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

        Trainee trainee = Trainee.builder()
                .userId(1L)
                .firstName("firstName")
                .lastName("lastName")
                .username("username")
                .password("password")
                .isActive(false).
                dateOfBirth(LocalDate.of(2024, 12, 12))
                .address("address")
                .build();
        traineeService.add(trainee);
        when(traineeMockDAO.findById(1L)).thenReturn(null);
        traineeService.delete(1L);
        when(traineeMockDAO.findById(1L)).thenReturn(null);
        assertNull(traineeService.findById(1L));

    }
}