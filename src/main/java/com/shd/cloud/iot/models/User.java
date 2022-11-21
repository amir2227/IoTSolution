package com.shd.cloud.iot.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 20)
    private String username;

    @Size(max = 30)
    private String fullname;

    @NotBlank
    @Size(max = 11)
    private String phone;

    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    private String password;

    @NotBlank
    private String token;

    @OneToMany(mappedBy = "user")
    private List<Sensor> sensors;

    @OneToMany(mappedBy = "user")
    private List<Operator> operators;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable
    private Set<Role> roles = new HashSet<>();

    public User(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 11) String phone,
            String fullname, @NotBlank @Size(max = 120) String password) {
        this.username = username;
        this.phone = phone;
        this.fullname = fullname;
        this.password = password;
    }

}