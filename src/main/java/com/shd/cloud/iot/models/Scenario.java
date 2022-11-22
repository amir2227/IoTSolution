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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "scenario")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    private Boolean isActive;

    @OneToMany(mappedBy = "scenario", cascade = { CascadeType.REMOVE })
    private List<ScenarioOperators> target_operators;

    @OneToMany(mappedBy = "scenario", cascade = { CascadeType.REMOVE })
    private List<ScenarioSensors> effective_sensors;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
