package com.technoidentity.dto;

import com.technoidentity.enums.Gender;
import com.technoidentity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChatUserDto {
    private UUID id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String profileImage;

    private Gender gender;

    private String mobileNumber;

    private Date dateOfBirth;

    private UserStatus userStatus;

    private UUID conversationId;

    private Integer unreadMessageCount;

}
