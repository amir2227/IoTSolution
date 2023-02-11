package com.shd.cloud.iot.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "mqtt_user")
@NoArgsConstructor
@Getter
@Setter
public class MqttUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, unique = true)
    private String username;
    @Column(length = 100)
    private String password;
    @Column(length = 40)
    private String salt;
    @Column(name = "is_superuser")
    private Boolean isSuperuser;

    public MqttUser(String username, String password, String salt, Boolean isSuperuser) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.isSuperuser = isSuperuser;
    }
}
