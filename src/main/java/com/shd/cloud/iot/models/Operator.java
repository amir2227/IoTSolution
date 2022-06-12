package com.shd.cloud.iot.models;

import java.util.List;

import javax.persistence.CascadeType;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.GenerationType;

@Entity
@Table(name = "operators", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "name" }) })
@JsonIgnoreProperties(value = { "histories", "user", "shared", "location" })
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

    @OneToMany(mappedBy = "operator", cascade = CascadeType.REMOVE)
    private List<OperatorHistory> histories;

    @OneToMany(mappedBy = "operator", cascade = CascadeType.REMOVE)
    private List<ScenarioOperators> scenario_Operators;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "shared_users")
    private SharedDevice shared;

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

    public List<ScenarioOperators> getScenario_Operators() {
        return scenario_Operators;
    }

    public void setScenario_Operators(List<ScenarioOperators> scenario_Operators) {
        this.scenario_Operators = scenario_Operators;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SharedDevice getShared() {
        return shared;
    }

    public void setShared(SharedDevice shared) {
        this.shared = shared;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Operator other = (Operator) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

}
