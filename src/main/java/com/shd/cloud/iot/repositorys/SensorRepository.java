package com.shd.cloud.iot.repositorys;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.shd.cloud.iot.models.Sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Boolean existsByNameAndUser_id(String name, Long user_id);

    Optional<Sensor> findByDeviceId(UUID deviceId);
    List<Sensor> findByUser_id(Long user_id);
    List<Sensor> findByLastHealthCheckDateBefore(LocalDateTime time);

    Optional<Sensor> findByIdAndUser_id(Long id, Long user_id);

    @Query(value = "select * from sensors op where op.user_id= :id and (op.name like %:key% or op.type like %:key%)", nativeQuery = true)
    List<Sensor> search(@Param("key") String key, @Param("id") Long id);
}
