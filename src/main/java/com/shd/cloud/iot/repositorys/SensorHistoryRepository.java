package com.shd.cloud.iot.repositorys;

import java.util.Date;
import java.util.List;

import com.shd.cloud.iot.models.SensorHistory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorHistoryRepository extends MongoRepository<SensorHistory, String> {
        @Query("{'sensorId': {$eq: ?0}}")
        List<SensorHistory> findBySensorId(Long sensorId);
        @Query("{'updatedAt': { $lte:  ?0 } }")
        List<SensorHistory> findAllWithEndDate(Date endDate);
        @Query("{'updatedAt': { $gte:  ?0 } }")
        List<SensorHistory> findAllWithStartDate(Date startDate);
        @Query("{'updatedAt' : { $gte: ?0, $lte: ?1 } }")
        List<SensorHistory> findAllWithBetweenDate(Date startDate, Date endDate);
        SensorHistory findFirstBySensorIdOrderByLastUpdateDesc(Long sensorId);
}
