package com.technoidentity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponseDto {
    private String token;
    private String tokenType = "Bearer";
    private  UserDto user;

}
