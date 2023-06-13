package com.technoidentity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateUserDetailsApiResponse {
    private UserResponseDto userInfoDto;
    private String message;

}
