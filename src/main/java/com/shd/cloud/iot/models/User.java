package com.shd.cloud.iot.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"password"})
@Entity
@Table(name = "users")
@JsonIgnoreProperties(value = {"sensors", "operators", "password"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,length = 64,nullable = false)
    private String email;
    @Column(length = 64)
    private String fullname;
    @Column(length = 16, nullable = false, unique = true)
    private String phone;
    @Column(nullable = false)
    private String password;
    @Column(unique = true,nullable = false)
    private String token;
    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "user")
    private List<Sensor> sensors;
    @OneToMany(mappedBy = "user")
    private List<Operator> operators;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable
    private Set<Role> roles = new HashSet<>();

    public User(String email, String fullname, String phone, String password) {
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

}