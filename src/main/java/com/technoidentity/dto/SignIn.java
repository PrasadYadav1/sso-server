package com.technoidentity.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class SignIn {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
