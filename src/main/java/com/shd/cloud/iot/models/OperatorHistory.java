package com.shd.cloud.iot.models;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
@Document("operator")
public class OperatorHistory {
    @Id
    private String id;
    private Boolean state;
    private LocalDateTime updatedAt;

    private User operatedUser;
    private Operator operator;

    public OperatorHistory(Boolean state, Operator operator, User operatedUser) {
        this.state = state;
        this.updatedAt = LocalDateTime.now();
        this.operator = operator;
        this.operatedUser = operatedUser;
    }
}
