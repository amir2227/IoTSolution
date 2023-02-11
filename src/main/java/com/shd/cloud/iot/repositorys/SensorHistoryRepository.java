package com.shd.cloud.iot.repositorys;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.shd.cloud.iot.models.SensorHistory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorHistoryRepository extends MongoRepository<SensorHistory, String> {

        Boolean existsByDeviceId(UUID deviceId);
        Optional<SensorHistory> findFirstByDeviceIdOrderByLastUpdateDesc(UUID deviceId);
        @Query("{'deviceId': {$eq: ?0}}")
        List<SensorHistory> findByDeviceId(UUID deviceId);
        @Query("{'updatedAt': { $lte:  ?0 } }")
        List<SensorHistory> findAllWithEndDate(LocalDateTime endDate);
        @Query("{'updatedAt': { $gte:  ?0 } }")
        List<SensorHistory> findAllWithStartDate(LocalDateTime startDate);
        @Query("{'updatedAt' : { $gte: ?0, $lte: ?1 } }")
        List<SensorHistory> findAllWithBetweenDate(LocalDateTime startDate, LocalDateTime endDate);
}
