package com.technoidentity.entity;


import com.technoidentity.enums.MessageStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "conversation_read_receipts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ConversationReadReceipt extends SharedModel implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "message_id")
    private UUID messageId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "message_id", nullable = false, insertable = false, updatable = false)
    private ConversationMessage conversationMessage;


    @Column(name = "receiver_participant_id")
    private UUID receiverParticipantId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_participant_id", nullable = false, insertable = false, updatable = false)
    private ConversationParticipant receiverParticipant;

    @Column(name = "message_status")
    private MessageStatus messageStatus;

    private String description;

}
