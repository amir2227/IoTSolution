package com.shd.cloud.iot.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "mqtt_acl")
@NoArgsConstructor
@Getter
@Setter
public class MqttAcl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Integer allow;
    @Column
    private String ipaddr;
    @Column(length = 100)
    private String username;
    @Column(length = 100)
    private String clientid;
    @Column
    private Integer access;
    @Column(length = 150)
    private String topic;

    public MqttAcl(Integer allow, String ipaddr, String username, String clientid, Integer access, String topic) {
        this.allow = allow;
        this.ipaddr = ipaddr;
        this.username = username;
        this.clientid = clientid;
        this.access = access;
        this.topic = topic;
    }
}
