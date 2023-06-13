package com.technoidentity.entity;


import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "conversation_messages")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@TypeDef(name = "list-array",typeClass = ListArrayType.class)
public class ConversationMessage extends SharedModel implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "parentMessageId")
    private UUID parentMessageId;

    @Column(name = "conversation_id")
    private UUID conversationId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conversation_id", nullable = false, insertable = false, updatable = false)
    private Conversation conversation;


    @Column(name = "sender_participant_id")
    private UUID senderParticipantId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_participant_id", nullable = false, insertable = false, updatable = false)
    private ConversationParticipant senderParticipant;

    @Column(name = "body",columnDefinition="TEXT")
    private String body;


    @Type(type = "list-array")
    @Column(
            name = "media",
            columnDefinition = "text[]"
    )
    private List<String> media;

    private String description;


    @Column(name="conversation_date" ,columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private Date conversationDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="message_id")
    private List<ConversationReadReceipt> conversationReadReceipts = new ArrayList<>();

    @Override
    public String toString() {
        return "ConversationMessage{" +
                "id=" + id +
                ", parentMessageId=" + parentMessageId +
                ", conversationId=" + conversationId +
                ", senderParticipantId=" + senderParticipantId +
                ", body='" + body + '\'' +
                ", media=" + media +
                ", description='" + description + '\'' +
                '}';
    }
}
