package com.shd.cloud.iot.repositorys;

import java.util.Optional;

import com.shd.cloud.iot.models.ERole;
import com.shd.cloud.iot.models.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
