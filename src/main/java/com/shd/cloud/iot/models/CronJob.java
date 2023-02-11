package com.shd.cloud.iot.models;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "t_cron_jobs")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class CronJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String expression;

    @Column
    private Boolean state;

    @Column(length = 64)
    private String taskName;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private Operator operator;

    public CronJob(String expression, Boolean state, String taskName, Operator operator) {
        this.expression = expression;
        this.state = state;
        this.taskName = taskName;
        this.operator = operator;
        this.createdAt = LocalDateTime.now();
    }
}
