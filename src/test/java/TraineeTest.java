
import org.example.configs.ConfigTest;
import org.example.interfaces.DAO;
import org.example.interfaces.Loader;
import org.example.interfaces.Model;
import org.example.loaders.TraineeLoader;
import org.example.models.Trainee;
import org.example.services.TraineeService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.Map;


public class TraineeTest {
    @Test
    public void testTraineeServiceBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        TraineeService service =(TraineeService) context.getBean("traineeService");
        assertNotNull(service);
    }
    @Test
    public void testTraineeLoaderBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        Loader loader =(TraineeLoader) context.getBean("traineeLoader");
        assertNotNull(loader);
    }
    @Test
    public void testTraineeStorageBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        Map<Integer, Model> storage =(Map<Integer, Model> ) context.getBean("traineeStorage");
        assertNotNull(storage);
    }
    @Test
    public void testTraineeDAOBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        DAO dao =(DAO) context.getBean("traineeDAO");
        assertNotNull(dao);
    }
    @Test
    public void testAddingTrainee() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        TraineeLoader loader = (TraineeLoader) context.getBean("traineeLoader");
        loader.clear();
        TraineeService service =(TraineeService) context.getBean("traineeService");
        assertNotNull(service);
        Trainee trainee =new Trainee(1, "firstName1","lastName1","username1", "password1",false, new Date(),"address1");
        service.addTrainee(trainee);
        Trainee checkTrainee= service.getTraineeById(trainee.getUserId());
        assertEquals(trainee.getUserId(),checkTrainee.getUserId());
        assertEquals(trainee.getFirstName(),checkTrainee.getFirstName());
        assertEquals(trainee.getLastName(),checkTrainee.getLastName());
        assertEquals(trainee.getUsername(),checkTrainee.getUsername());
        assertEquals(trainee.getPassword(),checkTrainee.getPassword());
        assertEquals(trainee.getAddress(),checkTrainee.getAddress());
        assertEquals(trainee.getIsActive(),checkTrainee.getIsActive());

    }
    @Test
    public void testUpdatingTrainee() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        TraineeLoader loader = (TraineeLoader) context.getBean("traineeLoader");
        loader.clear();
        TraineeService service =(TraineeService) context.getBean("traineeService");
        assertNotNull(service);
        Trainee trainee =new Trainee(1, "firstName1","lastName1","username1", "password1",false, new Date(),"address1");
        service.addTrainee(trainee);
        Trainee secondTrainee=new Trainee(1, "firstName2","lastName2","username2", "password2",false, new Date(),"address2");
        service.updateTrainee(1,secondTrainee);
        Trainee checkTrainee= service.getTraineeById(secondTrainee.getUserId());
        assertEquals(secondTrainee.getUserId(),checkTrainee.getUserId());
        assertEquals(secondTrainee.getFirstName(),checkTrainee.getFirstName());
        assertEquals(secondTrainee.getLastName(),checkTrainee.getLastName());
        assertEquals(secondTrainee.getUsername(),checkTrainee.getUsername());
        assertEquals(secondTrainee.getPassword(),checkTrainee.getPassword());
        assertEquals(secondTrainee.getAddress(),checkTrainee.getAddress());
        assertEquals(secondTrainee.getIsActive(),checkTrainee.getIsActive());
    }
    @Test
    public void testDeletingTrainee() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        TraineeLoader loader = (TraineeLoader) context.getBean("traineeLoader");
        loader.clear();
        TraineeService service =(TraineeService) context.getBean("traineeService");
        assertNotNull(service);
        Trainee trainee =new Trainee(1, "firstName1","lastName1","username1", "password1",false, new Date(),"address1");
        service.addTrainee(trainee);
        service.deleteTrainee(trainee.getUserId());
        assertNull(service.getTraineeById(trainee.getUserId()));
    }
}