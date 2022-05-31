package com.shd.cloud.iot.sevices;

import com.shd.cloud.iot.models.ERole;
import com.shd.cloud.iot.models.Role;
import com.shd.cloud.iot.repositorys.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartUp {

    @Autowired
    private RoleRepository repo;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        Long roles = repo.count();
        if(roles < 2){ 
        repo.save(new Role(ERole.ROLE_ADMIN));
        repo.save(new Role(ERole.ROLE_USER));
        }
    }
}
