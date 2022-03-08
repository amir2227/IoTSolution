package com.shd.cloud.iot.sevices;

import java.util.HashSet;
import java.util.Set;

import com.shd.cloud.iot.dtos.payload.request.SignupRequest;
import com.shd.cloud.iot.dtos.payload.response.MessageResponse;
import com.shd.cloud.iot.models.ERole;
import com.shd.cloud.iot.models.Role;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.repositorys.RoleRepository;
import com.shd.cloud.iot.repositorys.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<?> create(SignupRequest signUpRequest) {
        // if (errors.hasErrors()) {
        // return ResponseEntity.badRequest()
        // .body(errors.getAllErrors().stream()
        // .map((err) -> err.getArguments().toString() + ": " +
        // err.getDefaultMessage()));
        // }
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByPhone(signUpRequest.getPhone())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Phone is already in use!"));
        }
        // Create new user's account

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getPhone(),
                signUpRequest.getFullname(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setToken(tokenGenerator());
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("user created successfully!"));
    }

    private String tokenGenerator() {
        String t = RandomString.make(32);
        while (userRepository.existsByToken(t)) {
            t = RandomString.make(32);
        }
        return t;
    }

}
