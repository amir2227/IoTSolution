package com.shd.cloud.iot.sevices;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.List;

import com.shd.cloud.iot.exception.BadRequestException;
import com.shd.cloud.iot.exception.DuplicatException;
import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.enums.ERole;
import com.shd.cloud.iot.models.MqttAcl;
import com.shd.cloud.iot.models.MqttUser;
import com.shd.cloud.iot.models.Role;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.payload.request.EditUserRequest;
import com.shd.cloud.iot.payload.request.SignupRequest;
import com.shd.cloud.iot.repositorys.MqttAclRepository;
import com.shd.cloud.iot.repositorys.MqttUserRepository;
import com.shd.cloud.iot.repositorys.RoleRepository;
import com.shd.cloud.iot.repositorys.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MqttUserRepository mqttUserRepository;
    private final MqttAclRepository mqttAclRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    

    public User create(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicatException("Duplicate email");
        }
        if (userRepository.existsByPhone(signUpRequest.getPhone())) {
            throw new DuplicatException("Duplicate phone");
        }
        // Create new user's account
        User user = new User(signUpRequest.getEmail(),
                signUpRequest.getFullname(),
                signUpRequest.getPhone(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }
        user.setToken(tokenGenerator());
        user.setRoles(roles);
        MqttUser mqttUser = new MqttUser(user.getEmail(), encoder.encode(user.getToken()), null,false);
        MqttAcl mqttAcl = new MqttAcl(1,null, user.getEmail(), user.getToken(), 3,"device/%c/#");
        mqttUserRepository.save(mqttUser);
        mqttAclRepository.save(mqttAcl);
        return userRepository.save(user);
    }


    public User get(Long user_id) throws UsernameNotFoundException {
        return userRepository.findById(user_id)
                .orElseThrow(() -> new NotFoundException("User Not Found with id" + user_id));
    }


    public User getByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User Not Found with email" + email));
    }

    public List<User> search() {
        return userRepository.findAll();
    }

    public User Edit(Long user_id, EditUserRequest request) {
        User user = this.get(user_id);
        if (request.getPassword() != null) {
            if (request.getConfirm_password() == null ||
                    !request.getPassword().equals(request.getConfirm_password())) {
                throw new BadRequestException("invalid confirm password");
            }
            user.setPassword(encoder.encode(request.getPassword()));
        }
       
        if (request.getFullname() != null)
            user.setFullname(request.getFullname());
        if (request.getPhone() != null) {
            Optional<User> us = userRepository.findByPhone(request.getPhone());
            if (us.isPresent() && !us.get().getPhone().equals(request.getPhone()))
                throw new DuplicatException("phone already exist");

            user.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            Optional<User> us = userRepository.findByEmail(request.getEmail());
            if (us.isPresent() && !us.get().getEmail().equals(request.getEmail())) {
                throw new DuplicatException("username already exist");
            }
            MqttUser mqttUser = mqttUserRepository.findByUsername(user.getEmail()).get();
            List<MqttAcl> mqttAcls = mqttAclRepository.findByUsername(user.getEmail());
            mqttUser.setUsername(request.getEmail());
            for (MqttAcl mqttAcl: mqttAcls){
                mqttAcl.setUsername(mqttUser.getUsername());
                mqttAclRepository.save(mqttAcl);
            }
            mqttUserRepository.save(mqttUser);
            user.setEmail(request.getEmail());
        }
        return userRepository.save(user);
    }


    public String delete(Long id) {

        User user = this.get(id);
        try {
            userRepository.delete(user);
            return "successfully deleted";
        } catch (Exception e) {
            return "cannot be deleted!";
        }
    }

    private String tokenGenerator() {
        String t = RandomString.make(32);
        while (userRepository.existsByToken(t)) {
            t = RandomString.make(32);
        }
        return t;
    }

}
