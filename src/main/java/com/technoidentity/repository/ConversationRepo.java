package com.technoidentity.repository;

import com.technoidentity.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConversationRepo extends JpaRepository<Conversation, UUID> {

    Conversation findByNameContainingIgnoreCase(String name);
}
