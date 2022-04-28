package com.shd.cloud.iot.repositorys;

import java.util.List;

import com.shd.cloud.iot.models.OperatorHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorHistoryRepository extends JpaRepository<OperatorHistory, Long> {
    List<OperatorHistory> findByOperator_id(Long operator_id);

    @Query(value = "select * from operator_history where updated_at <= :endDate", nativeQuery = true)
    List<OperatorHistory> findAllWithEndDate(@Param("endDate") Long endDate);

    @Query(value = "select * from operator_history where updated_at >= :startDate", nativeQuery = true)
    List<OperatorHistory> findAllWithStartDate(@Param("startDate") Long startDate);

    @Query(value = "select * from operator_history where updated_at between :startDate and :endDate", nativeQuery = true)
    List<OperatorHistory> findAllWithBetweenDate(@Param("startDate") Long startDate,
            @Param("endDate") Long endDate);
}
