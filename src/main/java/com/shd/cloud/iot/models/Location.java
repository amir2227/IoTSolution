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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "locations")
@JsonIgnoreProperties({"user", "parent"})
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String name;
    @Column(length = 20)
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "location")
    private List<Operator> operators;

    @OneToMany(mappedBy = "location")
    private List<Sensor> sensors;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Location parent;
    @OneToMany(mappedBy = "parent")
    private List<Location> children;

    public Location(String name, String type, User user) {
        this.name = name;
        this.type = type;
        this.user = user;
    }

}
