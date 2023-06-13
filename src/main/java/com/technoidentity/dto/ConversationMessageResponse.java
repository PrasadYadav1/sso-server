package com.technoidentity.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ConversationMessageResponse {

    private UUID id;

    private UUID conversationId;

    private UUID senderParticipantId;
    private ConversationParticipantResponse senderParticipant;

    private String body;

    private List<String> media;

    private UUID parentMessageId;

    private String description;

    private Date conversationDate;

    private List<ConversationReadReceiptResponse> conversationReadReceipts = new ArrayList<>();

    public Date createdAt;

    private Date updatedAt;


    private Integer status;


}
