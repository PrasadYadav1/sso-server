package com.technoidentity.dto;

import com.technoidentity.entity.ConversationMessage;
import com.technoidentity.entity.ConversationParticipant;
import com.technoidentity.enums.MessageStatus;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConversationReadReceiptResponse {

    private UUID id;

    private UUID messageId;

    private UUID receiverParticipantId;

    private ConversationParticipantResponse receiverParticipant;

    private MessageStatus messageStatus;

    private String description;

}
