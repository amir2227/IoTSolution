package com.shd.cloud.iot.repositorys;

import java.util.List;
import java.util.Optional;

import com.shd.cloud.iot.models.Sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Boolean existsByNameAndUser_id(String name, Long user_id);

    List<Sensor> findByUser_id(Long user_id);

    Optional<Sensor> findByIdAndUser_id(Long id, Long user_id);
}
