package com.technoidentity.dto;

import com.technoidentity.enums.Gender;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Builder
public class SignUp {

    @NotBlank(message = "firstName is mandatory")
    private String firstName;

    private String middleName;

    @NotBlank(message = "lastName is mandatory")
    private String lastName;

    private Gender gender;

    @NotBlank(message = "email is mandatory")
    private String email;

    @NotBlank(message = "password is mandatory")
    private String password;

    @NotBlank(message = "confirm password is mandatory")
    private String confirmPassword;

    @NotBlank(message = "mobileNumber is mandatory")
    private String mobileNumber;

   private Date dateOfBirth;

}
