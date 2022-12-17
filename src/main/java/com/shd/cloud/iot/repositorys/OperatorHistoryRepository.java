package com.shd.cloud.iot.repositorys;

import java.util.Date;
import java.util.List;

import com.shd.cloud.iot.models.OperatorHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorHistoryRepository extends MongoRepository<OperatorHistory, String> {
    @Query("{'operatorId': {$eq: ?0}}")
    List<OperatorHistory> findByOperator_id(Long operator_id);
    @Query("{'updatedAt': { $lte:  ?0 } }")
    List<OperatorHistory> findAllWithEndDate(Date endDate);
    @Query("{'updatedAt': { $gte: ?0 } }")
    List<OperatorHistory> findAllWithStartDate(Date startDate);
    @Query("{'updatedAt' : { $gte: ?0, $lte: ?1 } }")
    List<OperatorHistory> findAllWithBetweenDate(Date startDate, Date endDate);
}
