package com.shd.cloud.iot.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "operator_history")
public class OperatorHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean state;

    @Column
    private Long updated_at;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "operator_id")
    private Operator operator;

    public OperatorHistory() {
    }

    public OperatorHistory(Boolean state, long updated_at, Operator operator) {
        this.state = state;
        this.updated_at = updated_at;
        this.operator = operator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Long updated_at) {
        this.updated_at = updated_at;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

}
