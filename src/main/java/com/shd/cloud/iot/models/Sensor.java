package com.shd.cloud.iot.models;

import java.time.LocalDateTime;
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
@Setter
@Getter
@EqualsAndHashCode
@Entity
@Table(name = "sensors", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "name" })
})
@JsonIgnoreProperties(value = { "histories", "user", "shared", "location" })
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Type(type = "uuid-char")
    @GeneratedValue
    private UUID deviceId;
    @NotNull
    @Column(length = 80)
    private String name;
    @NotNull
    @Column(length = 20)
    private String type;
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private DeviceStatus status;
    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdAt;
    @Column(columnDefinition = "timestamp")
    private LocalDateTime lastHealthCheckDate;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @OneToMany(mappedBy = "sensor")
    private List<ScenarioSensors> scenarios;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "shared_users")
    private SharedDevice shared;

    public Sensor(@NotNull String name, @NotNull String type) {
        this.name = name;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.lastHealthCheckDate = LocalDateTime.now();
    }


}
