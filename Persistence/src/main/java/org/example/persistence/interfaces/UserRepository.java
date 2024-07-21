package org.example.persistence.interfaces;


import org.example.model.User;

import java.util.Optional;

public interface UserRepository extends Repository<User, Integer> {
    User findByUsernameAndPassword(String username, String password);
    Optional<User> getAccountByUsername(String username);
}
