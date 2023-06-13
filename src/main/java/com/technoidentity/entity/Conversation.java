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
@Table(name = "conversations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Conversation extends SharedModel implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "conversation_type")
    private ConversationType conversationType;

    @Column(name = "name")
    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="conversation_id")
    private List<ConversationParticipant> conversationParticipants = new ArrayList<>();

}
