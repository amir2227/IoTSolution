package com.shd.cloud.iot.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EditUserRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    private String fullname;
    @NotBlank
    @Size(min = 11, max = 11)
    private String phone;
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    @NotBlank
    @Size(min = 6, max = 40)
    private String confirm_password;
}
