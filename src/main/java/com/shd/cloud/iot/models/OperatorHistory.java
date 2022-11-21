package com.shd.cloud.iot.models;

import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Setter
@Getter
@Document("operator")
public class OperatorHistory {
    @Id
    private String id;
    private Boolean state;
    private Long updated_at;

    private Long operator_id;

    public OperatorHistory(Boolean state, long updated_at, Long operator_id) {
        this.state = state;
        this.updated_at = updated_at;
        this.operator_id = operator_id;
    }
}
