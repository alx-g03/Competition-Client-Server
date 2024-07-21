package org.example.network.dtos;

import java.io.Serializable;

public class ChallengeDto implements Serializable {
    private String type;
    private String ageCategory;

    public ChallengeDto(String type, String ageCategory) {
        this.type = type;
        this.ageCategory = ageCategory;
    }

    public String getType() {
        return type;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    @Override
    public String toString() {
        return "ChallengeDto{" +
                "type='" + type + '\'' +
                ", ageCategory='" + ageCategory + '\'' +
                '}';
    }
}
