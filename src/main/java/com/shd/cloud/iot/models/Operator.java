package com.shd.cloud.iot.models;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shd.cloud.iot.enums.DeviceStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
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

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private DeviceStatus status;
    @NotNull
    @Column(length = 20)
    private String type;

    @Column
    private Date lastHealthCheckDate;

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

    public Operator(@NotNull String name, @NotNull Boolean state, @NotNull String type) {
        this.name = name;
        this.state = state;
        this.type = type;
        this.lastHealthCheckDate = new Date();
    }

}
