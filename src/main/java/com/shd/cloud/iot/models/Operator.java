package com.shd.cloud.iot.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GenerationType;

@Entity
@Table(name = "operators", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "name" }) })
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 80)
    private String name;

    // on = true & off = false
    @NotNull
    private Boolean state;

    @NotNull
    @Column(length = 20)
    private String type;

    @OneToMany(mappedBy = "operator")
    private List<OperatorHistory> histories;

    @OneToMany(mappedBy = "operator")
    private List<OperatorSensor> relations;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public Operator() {
    }

    public Operator(@NotNull String name, @NotNull Boolean state, @NotNull String type) {
        this.name = name;
        this.state = state;
        this.type = type;
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

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<OperatorHistory> getHistories() {
        return histories;
    }

    public void setHistories(List<OperatorHistory> histories) {
        this.histories = histories;
    }

    public List<OperatorSensor> getRelations() {
        return relations;
    }

    public void setRelations(List<OperatorSensor> relations) {
        this.relations = relations;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
