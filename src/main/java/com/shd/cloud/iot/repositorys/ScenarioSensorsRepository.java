package com.shd.cloud.iot.repositorys;

import com.shd.cloud.iot.models.ScenarioSensors;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioSensorsRepository extends JpaRepository<ScenarioSensors, Long> {
    List<ScenarioSensors> findBySensor_id(Long sensor_id);
}
