package com.shd.cloud.iot.sevices;

import com.shd.cloud.iot.enums.ERole;
import com.shd.cloud.iot.models.Role;
import com.shd.cloud.iot.repositorys.RoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartUp {
    private final RoleRepository repo;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        long roles = repo.count();
        if(roles < 2){ 
        repo.save(new Role(ERole.ROLE_ADMIN));
        repo.save(new Role(ERole.ROLE_USER));
        }
    }
}
