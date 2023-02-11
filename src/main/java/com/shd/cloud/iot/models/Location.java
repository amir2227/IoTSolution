package com.shd.cloud.iot.models;

import java.time.LocalDateTime;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "locations")
@JsonIgnoreProperties({"user", "parent"})
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String name;
    @Column(length = 20)
    private String type;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private JsonNode geometric;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "location")
    private List<Operator> operators;

    @OneToMany(mappedBy = "location")
    private List<Sensor> sensors;


    public Location(String name, String type, User user) {
        this.name = name;
        this.type = type;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

}

