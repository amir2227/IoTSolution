package com.shd.cloud.iot.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// target operators
@Entity
@Table(name = "scenario_operators")
@JsonIgnoreProperties({"operator", "scenario"})
@NoArgsConstructor
@Getter
@Setter
public class ScenarioOperators {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private Operator operator;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @Column
    private Boolean operator_state;

    public ScenarioOperators(Operator operator,  Boolean operator_state) {
        this.operator = operator;
        this.operator_state = operator_state;
    }
}
