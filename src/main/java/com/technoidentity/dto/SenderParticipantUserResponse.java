package com.technoidentity.dto;

import com.technoidentity.enums.Gender;
import com.technoidentity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SenderParticipantUserResponse {
    private UUID id;

    private String firstName;

    private String middleName;

    private String lastName;

}
