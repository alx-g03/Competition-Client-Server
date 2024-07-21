package org.example.network.dtos;

import java.io.Serializable;

public class ParticipationInfoDto implements Serializable {
    String name;
    Integer age;
    String type;

    public ParticipationInfoDto(String name, Integer age, String type) {
        this.name = name;
        this.age = age;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ParticipationInfoDto{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", type='" + type + '\'' +
                '}';
    }
}
