package com.shd.cloud.iot.repositorys;

import com.shd.cloud.iot.models.SharedDevice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedDeviceRepository extends JpaRepository<Long, SharedDevice> {
    
}
