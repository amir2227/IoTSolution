package com.shd.cloud.iot.repositorys;

import com.shd.cloud.iot.models.OperatorHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorHistoryRepository extends JpaRepository<OperatorHistory, Long> {

}
