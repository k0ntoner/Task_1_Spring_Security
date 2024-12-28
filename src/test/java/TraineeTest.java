import org.example.configs.Config;
import org.example.models.Trainee;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;


public class TraineeTest {
    @Test
    public void testTraineeAdding() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Map<Integer, Trainee> traineeMap = (Map<Integer, Trainee>) context.getBean("traineeStorage");
        System.out.println(traineeMap);
    }
}
