package org.example.model;

import java.io.Serializable;

public class Challenge implements Serializable {
    private Integer id;
    private String type;
    private String ageCategory;
    private Integer noParticipants;

    public Challenge() {
    }

    public Challenge(Integer id, String type, String ageCategory, Integer noParticipants) {
        this.id = id;
        this.type = type;
        this.ageCategory = ageCategory;
        this.noParticipants = noParticipants;
    }

    public Challenge(String type, String ageCategory, Integer noParticipants) {
        this.type = type;
        this.ageCategory = ageCategory;
        this.noParticipants = noParticipants;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(String ageCategory) {
        this.ageCategory = ageCategory;
    }

    public Integer getNoParticipants() {
        return noParticipants;
    }

    public void setNoParticipants(Integer noParticipants) {
        this.noParticipants = noParticipants;
    }

    @Override
    public String toString() {
        return "org.example.model.Challenge{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", ageCategory='" + ageCategory + '\'' +
                ", noParticipants=" + noParticipants +
                '}';
    }
}
