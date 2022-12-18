package com.shd.cloud.iot.payload.response;

import com.shd.cloud.iot.models.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String fullname;
    private String phone;
    private String token;
    private Set<Role> roles;
}
