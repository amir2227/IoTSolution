package com.shd.cloud.iot.repositorys;

import com.shd.cloud.iot.models.SensorHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorHistoryRepository extends JpaRepository<SensorHistory, Long> {

}
