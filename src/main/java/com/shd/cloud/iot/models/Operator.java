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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GenerationType;

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

    @NotNull
    @Column(length = 20)
    private String type;

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
    }

}
