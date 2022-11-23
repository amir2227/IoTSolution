package com.shd.cloud.iot.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JwtResponse {
    private String accessToken;
    private String type;
    private Long id;
    private String username;
    private String phone;
    private List<String> roles;
}