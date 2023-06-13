package com.technoidentity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotificationDto {
    private UUID id;

    private UUID conversationId;

    private UUID senderParticipantId;

    private SenderParticipantResponse senderParticipant;

    private String body;

    private List<String> media;

    private UUID parentMessageId;

    private String description;

    private Date conversationDate;
}
