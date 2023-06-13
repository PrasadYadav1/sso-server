package com.technoidentity.repository;

import com.technoidentity.entity.ConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConversationMessageRepo extends JpaRepository<ConversationMessage, UUID> {
    List<ConversationMessage> findByConversationId(UUID conversationId);

    List<ConversationMessage> findAllByConversationId(UUID conversationId);
}
