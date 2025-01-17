package org.example.repositories;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.User;
import org.example.utils.UserUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class TrainerDaoImpl implements TrainerDao {
    private SessionFactory sessionFactory;
    @Autowired
    public TrainerDaoImpl(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }



    @Override
    public Optional<Trainer> save(Trainer entity) {
        Transaction transaction =null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            if(entity.getId()==null){
                session.persist(entity);
                log.info("Saved new Trainer: {}", entity);
            }
            else {
                entity=session.merge(entity);
                log.info("Updated Trainer: {}", entity);
            }
            transaction.commit();
            return Optional.of(entity);
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Trainer> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Trainer entity = session.get(Trainer.class, id);
            log.info("Found Trainer: {}", entity);
            return Optional.of(entity);
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return Optional.empty();

    }

    @Override
    public Optional<Collection<Trainer>> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Collection<Trainer> entities = session.createQuery("select t from Trainer t",Trainer.class).list();
            log.info("Found {} Trainers", entities.size());
            return Optional.of(entities);
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return Optional.empty();
    }


    @Override
    public boolean isUsernameExist(String username) {
        try (Session session = sessionFactory.openSession()) {
            User user =session.createQuery("from User u where u.username=:username",User.class).setParameter("username", username).uniqueResult();
            return user!=null;
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return false;
    }
    @Override
    public boolean isPasswordMatch(Trainer entity, String password){
        return UserUtils.passwordMatch(password, entity.getPassword());
    }
    @Override
    public Optional<Trainer> findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Trainer entity = session.createQuery(
                            "FROM Trainer t where t.username = :username", Trainer.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return Optional.of(entity);

        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return Optional.empty();
    }
    @Override
    public Optional<Collection<Trainer>> findTrainersNotAssignedToTrainee(String traineeUsername) {
        try (Session session = sessionFactory.openSession()) {
            List<Trainer> trainers=session.createQuery("from Trainer t where t.id Not in (Select trainer.id from Training trainer join Trainee trainee where trainee.username!=:username  )", Trainer.class)
                    .setParameter("username", traineeUsername)
                    .list();
            return Optional.of(trainers);
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

}
