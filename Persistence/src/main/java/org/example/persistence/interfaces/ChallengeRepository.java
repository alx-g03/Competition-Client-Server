package org.example.persistence.interfaces;


import org.example.model.Challenge;

public interface ChallengeRepository extends Repository<Challenge, Integer> {
    Iterable<Challenge> findChallengesByType(String type);
    Iterable<Challenge> findChallengesByAgeCategory(String ageCategory);
    Iterable<Challenge> findChallengesByNoParticipants(Integer noParticipants);
    Challenge findChallengeByTypeAndAgeCategory(String type, String ageCategory);
}
