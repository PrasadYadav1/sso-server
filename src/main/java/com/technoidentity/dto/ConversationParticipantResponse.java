package com.technoidentity.dto;

import com.technoidentity.entity.Conversation;
import com.technoidentity.entity.User;
import lombok.*;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConversationParticipantResponse {
    private UUID id;

    private UUID conversationId;

    private Conversation conversation;

    private UUID userId;

    private UserResponseDto user;

    private String description;


}
