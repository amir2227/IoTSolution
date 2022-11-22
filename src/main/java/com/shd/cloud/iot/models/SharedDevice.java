package com.shd.cloud.iot.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@NoArgsConstructor
@Getter
@Setter
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
}
