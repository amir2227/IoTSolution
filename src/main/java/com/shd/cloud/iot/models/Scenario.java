package com.shd.cloud.iot.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "scenario")
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "scenario")
    private List<ScenarioOperators> target_operators;

    @JsonIgnore
    @OneToMany(mappedBy = "scenario")
    private List<ScenarioSensors> effective_sensors;

    public Scenario() {
    }

    public Scenario(String name, List<ScenarioOperators> target_operators, List<ScenarioSensors> effective_sensors) {
        this.name = name;
        this.target_operators = target_operators;
        this.effective_sensors = effective_sensors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
