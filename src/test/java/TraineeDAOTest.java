import org.example.configs.ConfigTest;
import org.example.loaders.TraineeLoader;
import org.example.models.Trainee;
import org.example.repositories.UserDAO;
import org.example.repositories.TraineeDAOImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeDAOTest {
    @Test
    public void testAddingTrainee() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        try {
            TraineeLoader loader = (TraineeLoader) context.getBean("traineeLoader");
            loader.clear();
            UserDAO<Trainee> traineeDAO = (TraineeDAOImpl) context.getBean("traineeDAOImpl");
            assertNotNull(traineeDAO);
            Trainee trainee = Trainee.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .username("username")
                    .isActive(false).
                    dateOfBirth(LocalDate.of(2024, 12, 12))
                    .address("address")
                    .build();
            traineeDAO.add(trainee);
            Trainee checkTrainee = traineeDAO.findById(trainee.getUserId());
            assertEquals(trainee.getUserId(), checkTrainee.getUserId());
            assertEquals(trainee.getFirstName(), checkTrainee.getFirstName());
            assertEquals(trainee.getLastName(), checkTrainee.getLastName());
            assertEquals(trainee.getUsername(), checkTrainee.getUsername());
            assertEquals(trainee.getPassword(), checkTrainee.getPassword());
            assertEquals(trainee.getAddress(), checkTrainee.getAddress());
            assertEquals(trainee.isActive(), checkTrainee.isActive());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        context.close();
    }
    @Test
    public void testUpdatingTrainee() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        try {
            TraineeLoader loader = (TraineeLoader) context.getBean("traineeLoader");
            loader.clear();
            UserDAO<Trainee> traineeDAO = (TraineeDAOImpl) context.getBean("traineeDAOImpl");
            assertNotNull(traineeDAO);
            Trainee trainee = Trainee.builder()
                    .firstName("firstName1")
                    .lastName("lastName1")
                    .username("username1")
                    .isActive(false).
                    dateOfBirth(LocalDate.of(2024, 12, 12))
                    .address("address1")
                    .build();
            traineeDAO.add(trainee);
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
            traineeDAO.update(1, secondTrainee);
            Trainee checkTrainee = traineeDAO.findById(secondTrainee.getUserId());
            assertEquals(secondTrainee.getUserId(), checkTrainee.getUserId());
            assertEquals(secondTrainee.getFirstName(), checkTrainee.getFirstName());
            assertEquals(secondTrainee.getLastName(), checkTrainee.getLastName());
            assertEquals(secondTrainee.getUsername(), checkTrainee.getUsername());
            assertEquals(secondTrainee.getPassword(), checkTrainee.getPassword());
            assertEquals(secondTrainee.getAddress(), checkTrainee.getAddress());
            assertEquals(secondTrainee.isActive(), checkTrainee.isActive());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        context.close();
    }
    @Test
    public void testDeletingTrainee() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        try {
            TraineeLoader loader = (TraineeLoader) context.getBean("traineeLoader");
            loader.clear();
            UserDAO<Trainee> traineeDAO = (TraineeDAOImpl) context.getBean("traineeDAOImpl");
            assertNotNull(traineeDAO);
            Trainee trainee = Trainee.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .username("username")
                    .isActive(false).
                    dateOfBirth(LocalDate.of(2024, 12, 12))
                    .address("address")
                    .build();
            Trainee checkTrainee = traineeDAO.add(trainee);
            traineeDAO.delete(checkTrainee.getUserId());
            assertNull(traineeDAO.findById(checkTrainee.getUserId()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        context.close();
    }
}
