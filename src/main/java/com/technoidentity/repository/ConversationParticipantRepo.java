package com.technoidentity.repository;

import com.technoidentity.entity.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConversationParticipantRepo extends JpaRepository<ConversationParticipant, UUID> {
    List<ConversationParticipant> findAllByConversationId(UUID conversationId);

    List<ConversationParticipant> findByConversationId(UUID conversationId);

    List<ConversationParticipant> findByUserId(UUID userId);

    @Query("SELECT c1.conversationId from ConversationParticipant c1 \n" +
            "INNER JOIN Conversation c2 ON c2.id=c1.conversationId \n" +
            "INNER JOIN  ConversationParticipant c3  ON c1.conversationId=c3.conversationId\n" +
            "WHERE c2.conversationType=0 AND c1.userId=:senderId AND c3.userId=:receiverId")
    UUID findBySingleConversationIdExist(@Param("senderId") UUID senderId,@Param("receiverId") UUID receiverId);

    ConversationParticipant findByConversationIdAndUserId(UUID conversationId, UUID id);
}
