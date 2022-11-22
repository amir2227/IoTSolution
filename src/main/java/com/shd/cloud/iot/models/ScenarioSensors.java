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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shd.cloud.iot.enums.EModality;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//effective sensors
@Entity
@Table(name = "scenario_sensors")
@JsonIgnoreProperties({"sensor", "operator", "scenario"})
@NoArgsConstructor
@Getter
@Setter
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
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;
}
