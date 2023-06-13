package com.technoidentity.dto;


import com.technoidentity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;

    private UUID roleId;

    private String roleName;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String profileImage;

    private Gender gender;

    private String mobileNumber;

    private Date dateOfBirth;


   }

