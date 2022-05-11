package com.shd.cloud.iot.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


//effective sensors
@Entity(name = "senario_sensors")
public class ScenarioSensors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EModality modality;

    @Column(length = 30)
    private String points;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = true)
    private Sensor sensor;

    @ManyToOne
    @JoinColumn(name = "operator_id", nullable = true)
    private Operator operator;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    public ScenarioSensors() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EModality getModality() {
        return modality;
    }

    public void setModality(EModality modality) {
        this.modality = modality;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

}
