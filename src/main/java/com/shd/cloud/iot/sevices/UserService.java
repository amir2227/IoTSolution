package com.shd.cloud.iot.sevices;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.List;

import com.shd.cloud.iot.exception.BadRequestException;
import com.shd.cloud.iot.exception.DuplicatException;
import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.enums.ERole;
import com.shd.cloud.iot.models.Role;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.payload.request.EditUserRequest;
import com.shd.cloud.iot.payload.request.SignupRequest;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    

    public User create(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new DuplicatException("Username");
        }
        if (userRepository.existsByPhone(signUpRequest.getPhone())) {
            throw new DuplicatException("Phone");
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

        return userRepository.save(user);
    }


    public User get(Long user_id) throws UsernameNotFoundException {
        return userRepository.findById(user_id)
                .orElseThrow(() -> new NotFoundException("User Not Found with id" + user_id));
    }


    public User getByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User Not Found with username" + username));
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
        if (request.getUsername() != null) {
            Optional<User> us = userRepository.findByUsername(request.getUsername());
            if (us.isPresent() && !us.get().getUsername().equals(request.getUsername()))
                throw new DuplicatException("username already exist");
            else
                user.setUsername(request.getUsername());
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
