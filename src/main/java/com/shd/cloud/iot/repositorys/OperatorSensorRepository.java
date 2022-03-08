package com.shd.cloud.iot.repositorys;

import com.shd.cloud.iot.models.OperatorSensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorSensorRepository extends JpaRepository<OperatorSensor, Long> {

}
