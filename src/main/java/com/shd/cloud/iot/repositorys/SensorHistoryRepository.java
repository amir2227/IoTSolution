package com.shd.cloud.iot.repositorys;

import java.util.List;

import com.shd.cloud.iot.models.SensorHistory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorHistoryRepository extends MongoRepository<SensorHistory, String> {
        @Query("{'sensor_id': {$eq: ?0}}")
        List<SensorHistory> findBySensor_id(Long sensor_id);
        @Query("{'updated_at': { $lte:  ?0 } }")
        List<SensorHistory> findAllWithEndDate(Long endDate);
        @Query("{'updated_at': { $gte:  ?0 } }")
        List<SensorHistory> findAllWithStartDate(Long startDate);
        @Query("{'updated_at' : { $gte: ?0, $lte: ?1 } }")
        List<SensorHistory> findAllWithBetweenDate(Long startDate, Long endDate);
        SensorHistory findFirstBySensorIdOrderByLastUpdateDesc(Long sensor_id);
}
