package org.example.repositories;

import lombok.extern.slf4j.Slf4j;

import org.example.repositories.entities.Trainee;
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
public class TraineeDaoImpl implements TraineeDao {
    private SessionFactory sessionFactory;
    @Autowired
    public TraineeDaoImpl(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Trainee> save(Trainee entity) {

        Transaction transaction =null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            if(entity.getId()==null){
                session.persist(entity);
                log.info("Saved new Trainee: {}", entity);
            }
            else {
                entity=session.merge(entity);
                log.info("Updated Trainee: {}", entity);
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
    public Optional<Trainee> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Trainee entity = session.get(Trainee.class, id);
            log.info("Found Trainee: {}", entity);
            return Optional.of(entity);
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Collection<Trainee>> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Collection<Trainee> entities = session.createQuery("select t from Trainee t",Trainee.class).list();
            log.info("Found {} Trainees", entities.size());
            return Optional.of(entities);
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void delete(Trainee entity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            if(!session.contains(entity)){
                entity=session.merge(entity);
            }
            session.remove(entity);
            transaction.commit();
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
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
    public boolean isPasswordMatch(Trainee entity, String password){
        return UserUtils.passwordMatch(password, entity.getPassword());
    }
    @Override
    public Optional<Trainee> findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Trainee entity = session.createQuery(
                            "FROM Trainee t where t.username = :username", Trainee.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return Optional.of(entity);

        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

}
