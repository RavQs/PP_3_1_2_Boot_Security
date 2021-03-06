package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public List<User> userList() {
        TypedQuery<User> query = entityManager.createQuery("select u from User u", User.class);

        return query.getResultList();
    }

    @Override
    public User findById(long id) {
        TypedQuery<User> query = entityManager.createQuery("select u from User u join fetch u.roles where u.id=:id", User.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void deleteById(long id) {
        entityManager.remove(findById(id));
    }

    @Override
    public void update(long id, User userUpdated) {
        User userForUpdate = findById(id);
        userForUpdate.setUsername(userUpdated.getUsername());
        userForUpdate.setPassword(userUpdated.getPassword());
        userForUpdate.setRoles(userUpdated.getRoles());
        entityManager.merge(userForUpdate);
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery("select u from User u join fetch u.roles where u.username=:username", User.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

}
