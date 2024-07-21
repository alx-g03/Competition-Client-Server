package org.example.services;

import org.example.model.Challenge;
import org.example.model.Participant;
import org.example.model.User;

import java.util.List;
import java.util.Optional;

public interface Services {
    Optional<User> login(String username, String password, UserObserver client);
    void logout(String username);
    List<Challenge> findAllChallenges();
    List<Participant> findParticipantsByChallengeTypeAndAgeCategory(String type, String ageCategory);
    void addParticipant(String name, Integer age, String type);
    void changeClient(UserObserver userObserver);
}
