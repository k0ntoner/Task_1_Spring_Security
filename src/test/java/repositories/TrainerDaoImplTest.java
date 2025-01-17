package repositories;


import org.example.repositories.TrainerDaoImpl;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.TrainingType;
import org.example.utils.UserUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class TrainerDaoImplTest {
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Transaction transaction;
    @InjectMocks
    private TrainerDaoImpl trainerDao;

    private Trainer testTrainer;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTrainer = buildTrainerForAdding();

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.merge(testTrainer)).thenReturn(testTrainer);
        doAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            trainer.setId(1L);
            return null;
        }).when(session).persist(any(Trainer.class));

    }

    public Trainer buildTrainerForAdding() {
        return Trainer.builder()
                .firstName("Mike")
                .lastName("Tyson")
                .username("Iron.Mike")
                .password(UserUtils.hashPassword("Champion"))
                .specialization("Boxing")
                .trainingType(TrainingType.STRENGTH)
                .isActive(true)
                .build();
    }
    @Test
    @DisplayName("Should return new Trainer")
    public void save_ShouldSaveNewTrainer() {
        Optional<Trainer> newTrainer = trainerDao.save(testTrainer);
        assertTrue(newTrainer.isPresent());
        assertNotNull(newTrainer.get().getId());
        assertEquals(testTrainer.getUsername(), newTrainer.get().getUsername());
        assertEquals(testTrainer.getFirstName(), newTrainer.get().getFirstName());
        assertEquals(testTrainer.getLastName(), newTrainer.get().getLastName());
        assertEquals(testTrainer.getSpecialization(), newTrainer.get().getSpecialization());
        assertEquals(testTrainer.getTrainingType(), newTrainer.get().getTrainingType());
        assertEquals(testTrainer.getPassword(), newTrainer.get().getPassword());
    }

    @Test
    @DisplayName("Should return updated Trainer")
    public void update_ShouldUpdateTrainer() {

        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newUsername = "newUsername";
        String newPassword = UserUtils.hashPassword("newPass");
        TrainingType newTrainingType = TrainingType.CARDIO;
        String newSpecialization = "newSpecialization";

        Trainer newTrainer = trainerDao.save(testTrainer).get();

        newTrainer.setFirstName(newFirstName);
        newTrainer.setLastName(newLastName);
        newTrainer.setUsername(newUsername);
        newTrainer.setPassword(newPassword);
        newTrainer.setTrainingType(newTrainingType);
        newTrainer.setSpecialization(newSpecialization);

        Optional<Trainer> updatedTrainer = trainerDao.save(newTrainer);

        assertTrue(updatedTrainer.isPresent());
        assertNotNull(updatedTrainer.get().getId());
        assertEquals(newUsername, updatedTrainer.get().getUsername());
        assertEquals(newFirstName, updatedTrainer.get().getFirstName());
        assertEquals(newLastName, updatedTrainer.get().getLastName());
        assertEquals(newSpecialization, updatedTrainer.get().getSpecialization());
        assertEquals(newTrainingType, updatedTrainer.get().getTrainingType());
        assertTrue(UserUtils.passwordMatch("newPass", updatedTrainer.get().getPassword()));

    }

    @Test
    @DisplayName("Should update not existing trainer")
    public void update_ShouldUpdateNotExistingTrainer() {
        testTrainer.setId(1L);
        when(session.merge(testTrainer)).thenThrow(HibernateException.class);
        assertFalse(trainerDao.save(testTrainer).isPresent());
    }
    @Test
    @DisplayName("Should find Trainer by id")
    public void findById_ShouldFindTraineeById() {
        Trainer newTrainer = trainerDao.save(testTrainer).get();

        assertNotNull(newTrainer);
        assertNotNull(newTrainer.getId());
        when(session.get(Trainer.class, 1L)).thenReturn(newTrainer);
        Trainer foundTrainer = trainerDao.findById(newTrainer.getId()).get();

        assertNotNull(foundTrainer);

        assertNotNull(newTrainer.getId());
        assertEquals(testTrainer.getUsername(), newTrainer.getUsername());
        assertEquals(testTrainer.getFirstName(), newTrainer.getFirstName());
        assertEquals(testTrainer.getLastName(), newTrainer.getLastName());
        assertEquals(testTrainer.getSpecialization(), newTrainer.getSpecialization());
        assertEquals(testTrainer.getTrainingType(), newTrainer.getTrainingType());
        assertEquals(testTrainer.getPassword(), newTrainer.getPassword());
    }

    @Test
    @DisplayName("Should find Collection of all Trainers")
    public void findAll_ShouldFindAllTrainers() {
        Trainer newTrainer=trainerDao.save(testTrainer).get();
        assertNotNull(newTrainer);
        List<Trainer> trainers=new ArrayList<>();
        trainers.add(newTrainer);
        Query<Trainer> mockQuery = mock(Query.class);

        when(session.createQuery("select t from Trainer t", Trainer.class)).thenReturn(mockQuery);

        when(mockQuery.list()).thenReturn(trainers);
        Collection<Trainer> foundTrainees=trainerDao.findAll().get();

        assertNotNull(trainers);
        assertEquals(trainers.size(), 1);

        trainers.forEach(
                trainer -> {
                    assertNotNull(trainer.getId());
                    assertNotNull(trainer.getUsername());
                    assertNotNull(trainer.getFirstName());
                    assertNotNull(trainer.getLastName());
                    assertNotNull(trainer.getSpecialization());
                    assertNotNull(trainer.getTrainingType());
                    assertNotNull(trainer.getPassword());
                    assertNotNull(trainer.getIsActive());
                }
        );
    }
    @Test
    @DisplayName("Should find Trainer by username")
    public void findByUsername_ShouldFindTrainerByUserName() {
        Trainer newTrainer=trainerDao.save(testTrainer).get();
        assertNotNull(newTrainer);
        assertNotNull(newTrainer.getId());

        Query<Trainer> mockQuery = mock(Query.class);

        when(session.createQuery("FROM Trainer t where t.username = :username", Trainer.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("username", newTrainer.getUsername())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(newTrainer);

        Trainer foundTrainer=trainerDao.findByUsername(newTrainer.getUsername()).get();
        assertNotNull(foundTrainer);
        assertEquals(newTrainer.getId(), foundTrainer.getId());
        assertEquals(newTrainer.getUsername(), foundTrainer.getUsername());
        assertEquals(newTrainer.getFirstName(), foundTrainer.getFirstName());
        assertEquals(newTrainer.getLastName(), foundTrainer.getLastName());
        assertEquals(newTrainer.getSpecialization(), foundTrainer.getSpecialization());
        assertEquals(newTrainer.getTrainingType(), foundTrainer.getTrainingType());
        assertEquals(newTrainer.getPassword(), foundTrainer.getPassword());
        assertEquals(newTrainer.getIsActive(), foundTrainer.getIsActive());
    }
}
