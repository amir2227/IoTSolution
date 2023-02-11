package com.shd.cloud.iot.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "shared_device")
@NoArgsConstructor
@Getter
@Setter
public class SharedDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "source_user_id")
    private User sourceUser;

    @OneToMany
    private List<User> targetUsers;

    @OneToMany(mappedBy = "shared")
    private List<Sensor> sensors;

    @OneToMany(mappedBy = "shared")
    private List<Operator> operators;

    public SharedDevice(User sourceUser, List<User> targetUsers, List<Sensor> sensors, List<Operator> operators) {
        this.sourceUser = sourceUser;
        this.targetUsers = targetUsers;
        this.sensors = sensors;
        this.operators = operators;
        this.createdAt = LocalDateTime.now();
    }
}
