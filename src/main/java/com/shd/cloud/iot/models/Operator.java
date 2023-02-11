package com.shd.cloud.iot.models;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shd.cloud.iot.enums.DeviceStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "operators", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "name" }) })
@JsonIgnoreProperties(value = { "histories", "user", "shared", "location", "cronJobs" })
public class Operator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "uuid-char")
    @GeneratedValue
    private UUID deviceId;
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

    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdAt;
    @Column(columnDefinition = "timestamp")
    private LocalDateTime lastHealthCheckDate;

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

    @OneToMany(mappedBy = "operator")
    private List<CronJob> cronJobs;

    public Operator(@NotNull String name, @NotNull Boolean state, @NotNull String type) {
        this.name = name;
        this.state = state;
        this.type = type;
        this.lastHealthCheckDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

}
