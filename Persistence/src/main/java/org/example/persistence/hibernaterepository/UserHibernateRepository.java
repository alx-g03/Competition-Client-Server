package org.example.persistence.hibernaterepository;

import org.example.model.User;
import org.example.persistence.interfaces.UserRepository;
import org.example.persistence.utils.HibernateUtils;
import org.hibernate.Session;

import java.util.Collection;
import java.util.Optional;

public class UserHibernateRepository implements UserRepository {
    @Override
    public Optional<User> getAccountByUsername(String username) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createSelectionQuery("from User where username=?1", User.class)
                    .setParameter(1, username)
                    .getSingleResultOrNull());
        }
    }

    @Override
    public void add(User elem) {

    }

    @Override
    public void delete(User elem) {

    }

    @Override
    public void update(User elem, Integer id) {

    }

    @Override
    public User findById(Integer id) {
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public Collection<User> getAll() {
        return null;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return null;
    }
}
