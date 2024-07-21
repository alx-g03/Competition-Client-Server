package org.example.persistence.interfaces;


import org.example.model.Participation;

public interface ParticipationRepository extends Repository<Participation, Integer> {
    Iterable<Participation> findParticipationsByParticipantId(Integer participantId);
    Iterable<Participation> findParticipationsByChallengeId(Integer challengeId);
}
