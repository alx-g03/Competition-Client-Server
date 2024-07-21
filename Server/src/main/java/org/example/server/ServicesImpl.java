package org.example.server;

import org.example.model.Challenge;
import org.example.model.Participant;
import org.example.model.Participation;
import org.example.model.User;
import org.example.persistence.interfaces.ChallengeRepository;
import org.example.persistence.interfaces.ParticipantRepository;
import org.example.persistence.interfaces.ParticipationRepository;
import org.example.persistence.interfaces.UserRepository;
import org.example.services.Services;
import org.example.services.UserObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImpl implements Services {
    private UserRepository userRepository;
    private ChallengeRepository challengeRepository;
    private ParticipantRepository participantRepository;
    private ParticipationRepository participationRepository;
    private Map<String, UserObserver> loggedClients;
    private final int defaultThreadsNo=5;

    public ServicesImpl(UserRepository userRepository, ChallengeRepository challengeRepository, ParticipantRepository participantRepository, ParticipationRepository participationRepository) {
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.participantRepository = participantRepository;
        this.participationRepository = participationRepository;
        loggedClients= new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Optional<User> login(String username, String password, UserObserver client) {
        var userOpt = userRepository.getAccountByUsername(username);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            var parolaDB = user.getPassword();
            if(!password.equals(parolaDB)){
                throw new IllegalArgumentException("Parola incorecta");
            }
            else {
                if (loggedClients.get(username) != null)
                    throw new IllegalArgumentException("Utilizatorul este deja autentificat");
                loggedClients.put(username, client);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public synchronized void logout(String username) {
        if (loggedClients.get(username) == null)
            throw new IllegalArgumentException("Utilizatorul nu este autentificat");
        loggedClients.remove(username);
    }

    @Override
    public List<Challenge> findAllChallenges() {
        return (List<Challenge>) challengeRepository.findAll();
    }

    @Override
    public List<Participant> findParticipantsByChallengeTypeAndAgeCategory(String type, String ageCategory) {
        Challenge challenge = challengeRepository.findChallengeByTypeAndAgeCategory(type, ageCategory);
        List<Participant> participants = new ArrayList<>();
        if (challenge != null) {
            List<Participation> participations = (List<Participation>) participationRepository.findParticipationsByChallengeId(challenge.getId());
            for (var participation : participations) {
                participants.add(participantRepository.findById(participation.getParticipantId()));
            }
        }
        else {
            throw new IllegalArgumentException("Challenge not found");
        }
        return participants;
    }

    @Override
    public synchronized void addParticipant(String name, Integer age, String type) {
        String ageCategory = null;
        if (age >= 6 && age <= 8) {
            ageCategory = "6-8 ani";
        } else if (age >= 9 && age <= 11) {
            ageCategory = "9-11 ani";
        } else if (age >= 12 && age <= 15) {
            ageCategory = "12-15 ani";
        }
        else {
            throw new IllegalArgumentException("Invalid age");
        }

        Challenge challenge = challengeRepository.findChallengeByTypeAndAgeCategory(type, ageCategory);
        if (challenge != null) {
            Participant participant = participantRepository.findParticipantByName(name);
            if (participant == null) {
                participant = new Participant(name, age, 1);
                participantRepository.add(participant);
                participant = participantRepository.findParticipantByName(name);
                Participation participation = new Participation(participant.getId(), challenge.getId());
                participationRepository.add(participation);
                Challenge updatedChallenge = new Challenge(challenge.getType(), challenge.getAgeCategory(), challenge.getNoParticipants() + 1);
                challengeRepository.update(updatedChallenge, challenge.getId());
            }
            else {
                if (participant.getNoChallenges() < 2) {
                    Participant updatedParticipant = new Participant(participant.getName(), participant.getAge(), participant.getNoChallenges() + 1);
                    participantRepository.update(updatedParticipant, participant.getId());
                    Participation participation = new Participation(participant.getId(), challenge.getId());
                    participationRepository.add(participation);
                    Challenge updatedChallenge = new Challenge(challenge.getType(), challenge.getAgeCategory(), challenge.getNoParticipants() + 1);
                    challengeRepository.update(updatedChallenge, challenge.getId());
                }
                else {
                    throw new IllegalArgumentException("Maximum challenges exceeded");
                }
            }
        }
        else {
            throw new IllegalArgumentException("Challenge not found");
        }
        notifyClients();
    }

    @Override
    public void changeClient(UserObserver userObserver){}

    private void notifyClients(){
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(var client : loggedClients.values()){
            if(client == null)
                continue;
            executor.execute(client::participationAdded);
        }
    }
}
