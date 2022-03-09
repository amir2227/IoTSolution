package com.shd.cloud.iot.repositorys;

import com.shd.cloud.iot.models.Operator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {
    Boolean existsByNameAndUser_id(String name, Long user_id);
}
