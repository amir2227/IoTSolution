package com.shd.cloud.iot.repositorys;

import com.shd.cloud.iot.models.ScenarioOperators;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioOperatorsRepository extends JpaRepository<ScenarioOperators, Long> {

}
