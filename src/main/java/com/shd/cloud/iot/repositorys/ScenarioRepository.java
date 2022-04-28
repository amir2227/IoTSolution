package com.shd.cloud.iot.repositorys;

import com.shd.cloud.iot.models.Scenario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

}
