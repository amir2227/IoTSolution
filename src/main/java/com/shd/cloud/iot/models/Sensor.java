package com.shd.cloud.iot.models;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shd.cloud.iot.enums.DeviceStatus;
import com.shd.cloud.iot.enums.ERole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @NotNull
    @Column(length = 80)
    private String name;
    @NotNull
    @Column(length = 20)
    private String type;
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private DeviceStatus status;
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
    }


}
