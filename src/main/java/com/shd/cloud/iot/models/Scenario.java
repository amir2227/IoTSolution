package com.shd.cloud.iot.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "scenario")
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "scenario", cascade = { CascadeType.REMOVE })
    private List<ScenarioOperators> target_operators;

    @OneToMany(mappedBy = "scenario", cascade = { CascadeType.REMOVE })
    private List<ScenarioSensors> effective_sensors;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Scenario() {
    }

    public Scenario(String description, List<ScenarioOperators> target_operators, List<ScenarioSensors> effective_sensors) {
        this.description = description;
        this.target_operators = target_operators;
        this.effective_sensors = effective_sensors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ScenarioOperators> getTarget_operators() {
        return target_operators;
    }

    public void setTarget_operators(List<ScenarioOperators> target_operators) {
        this.target_operators = target_operators;
    }

    public List<ScenarioSensors> getEffective_sensors() {
        return effective_sensors;
    }

    public void setEffective_sensors(List<ScenarioSensors> effective_sensors) {
        this.effective_sensors = effective_sensors;
    }

}
