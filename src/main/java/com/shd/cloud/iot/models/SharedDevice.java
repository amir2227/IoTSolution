package com.shd.cloud.iot.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "shared_device")
public class SharedDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_user_id")
    private User source_user;

    @OneToMany
    private List<User> target_users;

    @OneToMany(mappedBy = "shared")
    private List<Sensor> sensors;

    @OneToMany(mappedBy = "shared")
    private List<Operator> operators;

    public SharedDevice() {
    }

    public SharedDevice(User source_user, List<User> target_users, List<Sensor> sensors, List<Operator> operators) {
        this.source_user = source_user;
        this.target_users = target_users;
        this.sensors = sensors;
        this.operators = operators;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSource_user() {
        return source_user;
    }

    public void setSource_user(User source_user) {
        this.source_user = source_user;
    }

   
    public List<User> getTarget_users() {
        return target_users;
    }

    public void setTarget_users(List<User> target_users) {
        this.target_users = target_users;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public void setOperators(List<Operator> operators) {
        this.operators = operators;
    }

    
}
