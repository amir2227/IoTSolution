package com.shd.cloud.iot.repositorys;

import com.shd.cloud.iot.models.MqttUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MqttUserRepository extends JpaRepository<MqttUser, Long> {
    Optional<MqttUser> findByUsername(String username);
}
