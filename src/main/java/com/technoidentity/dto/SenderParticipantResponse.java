package com.technoidentity.dto;

import com.technoidentity.entity.Conversation;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SenderParticipantResponse {
    private UUID id;

    private UUID userId;

    private SenderParticipantUserResponse user;

    private String description;
}
