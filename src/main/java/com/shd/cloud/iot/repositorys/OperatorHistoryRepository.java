package com.shd.cloud.iot.repositorys;

import java.util.List;

import com.shd.cloud.iot.models.OperatorHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorHistoryRepository extends MongoRepository<OperatorHistory, String> {
    @Query("{'operator_id': {$eq: ?0}}")
    List<OperatorHistory> findByOperator_id(Long operator_id);
    @Query("{'updated_at': { $lte:  ?0 } }")
    List<OperatorHistory> findAllWithEndDate(Long endDate);
    @Query("{'updated_at': { $gte: ?0 } }")
    List<OperatorHistory> findAllWithStartDate(Long startDate);
    @Query("{'updated_at' : { $gte: ?0, $lte: ?1 } }")
    List<OperatorHistory> findAllWithBetweenDate(Long startDate, Long endDate);
}
