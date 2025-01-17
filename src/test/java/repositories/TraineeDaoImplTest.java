package repositories;


import org.example.repositories.impl.TraineeDaoImpl;
import org.example.repositories.entities.Trainee;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class TraineeDaoImplTest {
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Transaction transaction;
    @InjectMocks
    private TraineeDaoImpl traineeDao;

    private Trainee testTrainee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTrainee = buildTraineeForAdding();

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.merge(testTrainee)).thenReturn(testTrainee);
        doAnswer(invocation -> {
            Trainee trainee = invocation.getArgument(0);
            trainee.setId(1L);
            return null;
        }).when(session).persist(any(Trainee.class));

    }

    public Trainee buildTraineeForAdding() {
        return Trainee.builder()
                .firstName("John")
                .lastName("Doe")
                .username("John.Doe")
                .password(UserUtils.hashPassword("JohnDoe"))
                .dateOfBirth(LocalDate.of(1998, 11, 23))
                .address("Test Street")
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should return new Trainee")
    public void save_ShouldSaveNewTrainee() {
        Trainee newTrainee = traineeDao.save(testTrainee);

        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());
        assertEquals(testTrainee.getUsername(), newTrainee.getUsername());
        assertEquals(testTrainee.getFirstName(), newTrainee.getFirstName());
        assertEquals(testTrainee.getLastName(), newTrainee.getLastName());
        assertEquals(testTrainee.getDateOfBirth(), newTrainee.getDateOfBirth());
        assertEquals(testTrainee.getAddress(), newTrainee.getAddress());
        assertEquals(testTrainee.getPassword(), newTrainee.getPassword());
    }

    @Test
    @DisplayName("Should return updated Trainee")
    public void update_ShouldUpdateTrainee() {

        String newAddress = "newAddress";
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newUsername = "newUsername";
        LocalDate newDateOfBirth = LocalDate.of(2000, 11, 23);
        String newPassword = UserUtils.hashPassword("newPass");

        Trainee newTrainee = traineeDao.save(testTrainee);

        newTrainee.setAddress(newAddress);
        newTrainee.setFirstName(newFirstName);
        newTrainee.setLastName(newLastName);
        newTrainee.setUsername(newUsername);
        newTrainee.setPassword(newPassword);
        newTrainee.setDateOfBirth(newDateOfBirth);
        Trainee updatedTrainee = traineeDao.update(newTrainee);

        assertNotNull(updatedTrainee);
        assertNotNull(updatedTrainee.getId());
        assertEquals(newUsername, updatedTrainee.getUsername());
        assertEquals(newAddress, updatedTrainee.getAddress());
        assertEquals(newFirstName, updatedTrainee.getFirstName());
        assertEquals(newLastName, updatedTrainee.getLastName());
        assertEquals(newDateOfBirth, updatedTrainee.getDateOfBirth());
        assertTrue(UserUtils.passwordMatch("newPass", updatedTrainee.getPassword()));

    }

    @Test
    @DisplayName("Should update not existing trainee")
    public void update_ShouldUpdateNotExistingTrainee() {
        testTrainee.setId(1L);
        when(session.merge(testTrainee)).thenThrow(HibernateException.class);
        assertNull(traineeDao.update(testTrainee));
    }

    @Test
    @DisplayName("Should delete Trainee")
    public void delete_ShouldDeleteTrainee() {
        when(session.get(Trainee.class, 1L)).thenReturn(null);
        testTrainee.setId(1L);
        traineeDao.delete(testTrainee);
        assertFalse(traineeDao.findById(1L).isPresent());
    }

    @Test
    @DisplayName("Should find Trainee by id")
    public void findById_ShouldFindTraineeById() {
        Trainee newTrainee = traineeDao.save(testTrainee);

        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());
        when(session.get(Trainee.class, 1L)).thenReturn(newTrainee);
        Trainee foundTrainee = traineeDao.findById(newTrainee.getId()).get();

        assertNotNull(foundTrainee);
        assertEquals(newTrainee.getId(), foundTrainee.getId());
        assertEquals(newTrainee.getUsername(), foundTrainee.getUsername());
        assertEquals(newTrainee.getFirstName(), foundTrainee.getFirstName());
        assertEquals(newTrainee.getLastName(), foundTrainee.getLastName());
        assertEquals(newTrainee.getDateOfBirth(), foundTrainee.getDateOfBirth());
        assertEquals(newTrainee.getAddress(), foundTrainee.getAddress());
        assertEquals(newTrainee.getPassword(), foundTrainee.getPassword());
    }

    @Test
    @DisplayName("Should find Collection of all Trainees")
    public void findAll_ShouldFindAllTrainees() {
        Trainee newTrainee = traineeDao.save(testTrainee);
        assertNotNull(newTrainee);
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(newTrainee);
        Query<Trainee> mockQuery = mock(Query.class);

        when(session.createQuery("select t from Trainee t", Trainee.class)).thenReturn(mockQuery);

        when(mockQuery.list()).thenReturn(trainees);
        Collection<Trainee> foundTrainees = traineeDao.findAll();

        assertNotNull(trainees);
        assertEquals(trainees.size(), 1);

        trainees.forEach(
                trainee -> {
                    assertNotNull(trainee.getId());
                    assertNotNull(trainee.getUsername());
                    assertNotNull(trainee.getFirstName());
                    assertNotNull(trainee.getLastName());
                    assertNotNull(trainee.getDateOfBirth());
                    assertNotNull(trainee.getAddress());
                    assertNotNull(trainee.getPassword());
                }
        );
    }

    @Test
    @DisplayName("Should find Trainee by username")
    public void findByUsername_ShouldFindTraineeByUserName() {
        Trainee newTrainee = traineeDao.save(testTrainee);
        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());

        Query<Trainee> mockQuery = mock(Query.class);

        when(session.createQuery("FROM Trainee t where t.username = :username", Trainee.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("username", newTrainee.getUsername())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(newTrainee);

        Trainee foundTrainee = traineeDao.findByUsername(newTrainee.getUsername()).get();
        assertNotNull(foundTrainee);
        assertEquals(newTrainee.getId(), foundTrainee.getId());
        assertEquals(newTrainee.getUsername(), foundTrainee.getUsername());
        assertEquals(newTrainee.getFirstName(), foundTrainee.getFirstName());
        assertEquals(newTrainee.getLastName(), foundTrainee.getLastName());
        assertEquals(newTrainee.getDateOfBirth(), foundTrainee.getDateOfBirth());
        assertEquals(newTrainee.getAddress(), foundTrainee.getAddress());
        assertEquals(newTrainee.getPassword(), foundTrainee.getPassword());
    }


}
