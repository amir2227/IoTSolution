package com.shd.cloud.iot.repositorys;

import java.util.List;

import com.shd.cloud.iot.models.SensorHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorHistoryRepository extends JpaRepository<SensorHistory, Long> {

        List<SensorHistory> findBySensor_id(Long sensor_id);

        @Query(value = "select * from sensor_history where updated_at <= :endDate", nativeQuery = true)
        List<SensorHistory> findAllWithEndDate(@Param("endDate") Long endDate);

        @Query(value = "select * from sensor_history where updated_at >= :startDate", nativeQuery = true)
        List<SensorHistory> findAllWithStartDate(@Param("startDate") Long startDate);

        @Query(value = "select * from sensor_history where updated_at between :startDate and :endDate", nativeQuery = true)
        List<SensorHistory> findAllWithBetweenDate(@Param("startDate") Long startDate,
                        @Param("endDate") Long endDate);
}
