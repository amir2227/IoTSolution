package com.shd.cloud.iot.repositorys;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.shd.cloud.iot.models.Operator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {

    Boolean existsByNameAndUser_id(String name, Long user_id);
    List<Operator> findByLastHealthCheckDateBefore(LocalDateTime time);
    Optional<Operator> findByDeviceId(UUID deviceId);
    List<Operator> findByUser_id(Long user_id);

    Optional<Operator> findByIdAndUser_id(Long id, Long user_id);

    @Query(value = "select * from operators op where op.user_id= :id and (op.name like %:key% or op.type like %:key%)", nativeQuery = true)
    List<Operator> search(@Param("key") String key, @Param("id") Long id);
}
