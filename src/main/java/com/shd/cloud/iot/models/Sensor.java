package com.shd.cloud.iot.models;

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

@Entity
@Table(name = "sensors", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "name" })
})
public class Sensor {

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

    @OneToMany(mappedBy = "sensor")
    private List<OperatorSensor> relations;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
