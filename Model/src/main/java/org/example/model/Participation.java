package org.example.model;

import java.io.Serializable;

public class Participation implements Serializable {
    private Integer id;
    private Integer participantId;
    private Integer challengeId;

    public Participation() {
    }

    public Participation(Integer id, Integer participantId, Integer challengeId) {
        this.id = id;
        this.participantId = participantId;
        this.challengeId = challengeId;
    }

    public Participation(Integer participantId, Integer challengeId) {
        this.participantId = participantId;
        this.challengeId = challengeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Integer participantId) {
        this.participantId = participantId;
    }

    public Integer getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    @Override
    public String toString() {
        return "org.example.model.Participation{" +
                "id=" + id +
                ", participantId=" + participantId +
                ", challengeId=" + challengeId +
                '}';
    }
}
