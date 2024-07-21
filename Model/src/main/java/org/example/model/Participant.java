package org.example.model;

import java.io.Serializable;

public class Participant implements Serializable {
    private Integer id;
    private String name;
    private Integer age;
    private Integer noChallenges;

    public Participant() {
    }

    public Participant(Integer id, String name, Integer age, Integer noChallenges) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.noChallenges = noChallenges;
    }

    public Participant(String name, Integer age, Integer noChallenges) {
        this.name = name;
        this.age = age;
        this.noChallenges = noChallenges;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getNoChallenges() {
        return noChallenges;
    }

    public void setNoChallenges(Integer noChallenges) {
        this.noChallenges = noChallenges;
    }

    @Override
    public String toString() {
        return "org.example.model.Participant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", noChallenges=" + noChallenges +
                '}';
    }
}
