package com.shd.cloud.iot.repositorys;

import com.shd.cloud.iot.models.CronJobs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronJobsRepository extends JpaRepository<CronJobs, Long> {
    
}
