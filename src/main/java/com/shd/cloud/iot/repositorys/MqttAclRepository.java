package com.shd.cloud.iot.repositorys;

import com.shd.cloud.iot.models.MqttAcl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MqttAclRepository extends JpaRepository<MqttAcl,Long> {
    List<MqttAcl> findByUsername(String username);
}
