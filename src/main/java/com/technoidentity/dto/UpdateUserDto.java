package com.technoidentity.dto;

import com.technoidentity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UpdateUserDto {

    private String firstName;

    private String middleName;

    private String lastName;

    private String profileImage;

    private Gender gender;

    private String mobileNumber;

    private Date dateOfBirth;
}
