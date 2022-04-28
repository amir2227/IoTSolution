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

    public User() {
    }

    public User(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 11) String phone,
            String fullname, @NotBlank @Size(max = 120) String password) {
        this.username = username;
        this.phone = phone;
        this.fullname = fullname;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public void setOperators(List<Operator> operators) {
        this.operators = operators;
    }

    @Override
    public String toString() {
        return "User [fullname=" + fullname + ", id=" + id + ", phone=" + phone + ", roles=" + roles + ", token="
                + token + ", username=" + username + "]";
    }

}