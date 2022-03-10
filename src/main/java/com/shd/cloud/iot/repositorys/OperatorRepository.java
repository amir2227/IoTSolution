package com.shd.cloud.iot.repositorys;

import java.util.List;
import java.util.Optional;

import com.shd.cloud.iot.models.Operator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {
    Boolean existsByNameAndUser_id(String name, Long user_id);

    List<Operator> findByUser_id(Long user_id);

    Optional<Operator> findByIdAndUser_id(Long id, Long user_id);
}
