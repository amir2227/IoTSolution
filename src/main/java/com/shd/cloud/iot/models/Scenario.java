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
import javax.persistence.Table;

@Entity
@Table(name = "scenario")
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String name;

    @Column()
    private Boolean operator_state;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EModality modality;

    @Column(length = 30)
    private String points;

    @ManyToOne
    @JoinColumn(name = "operator2_id")
    private Operator operator2;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private Operator operator;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    public Scenario() {
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

    public Boolean getOperator_state() {
        return operator_state;
    }

    public void setOperator_state(Boolean operator_state) {
        this.operator_state = operator_state;
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

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Operator getOperator2() {
        return operator2;
    }

    public void setOperator2(Operator operator2) {
        this.operator2 = operator2;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

}
