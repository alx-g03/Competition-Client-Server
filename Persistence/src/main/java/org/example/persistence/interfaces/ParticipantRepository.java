package org.example.persistence.interfaces;


import org.example.model.Participant;

public interface ParticipantRepository extends Repository<Participant, Integer> {
    Participant findParticipantByName(String name);
    Iterable<Participant> findParticipantsByAge(Integer age);
    Iterable<Participant> findParticipantsByNoChallenges(Integer noChallenges);
}
