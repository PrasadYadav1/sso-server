package com.technoidentity.entity;


import com.technoidentity.enums.ConversationType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "conversation_participants")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ConversationParticipant extends SharedModel implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "conversation_id")
    private UUID conversationId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conversation_id", nullable = false, insertable = false, updatable = false)
    private Conversation conversation;


    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;


    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="sender_participant_id")
    private List<ConversationMessage> conversationMessages = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="receiver_participant_id")
    private List<ConversationReadReceipt> conversationReadReceipts = new ArrayList<>();

    @Override
    public String toString() {
        return "ConversationParticipant{" +
                "id=" + id +
                ", conversationId=" + conversationId +
                ", userId=" + userId +
                ", description='" + description + '\'' +
                '}';
    }
}
