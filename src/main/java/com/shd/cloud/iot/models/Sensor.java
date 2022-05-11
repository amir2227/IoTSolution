package com.shd.cloud.iot.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "sensors", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "name" })
})
@JsonIgnoreProperties(value = { "histories", "user", "shared" })
public class Sensor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 80)
    private String name;

    @NotNull
    @Column(length = 20)
    private String type;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "sensor")
    private List<SensorHistory> histories;

    @JsonIgnore
    @OneToMany(mappedBy = "sensor")
    private List<ScenarioSensors> scenarios;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "shared_users")
    private SharedDevice shared;

    public Sensor() {
    }

    public Sensor(@NotNull String name, @NotNull String type) {
        this.name = name;
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

    public List<SensorHistory> getHistories() {
        return histories;
    }

    public void setHistories(List<SensorHistory> histories) {
        this.histories = histories;
    }

    public List<ScenarioSensors> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<ScenarioSensors> scenarios) {
        this.scenarios = scenarios;
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

}
