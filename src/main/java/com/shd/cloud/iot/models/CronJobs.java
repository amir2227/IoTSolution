package com.shd.cloud.iot.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_cron_jobs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CronJobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String cronExpression;

    @Column
    private Boolean state;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "operator_id")
    private Operator operator;
}
