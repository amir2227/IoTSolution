package com.shd.cloud.iot.repositorys;

import java.util.Optional;

import com.shd.cloud.iot.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    Boolean existsByToken(String token);
}
