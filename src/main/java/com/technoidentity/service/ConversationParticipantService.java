package com.technoidentity.service;

import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ConversationParticipantService {
    UUID findBySingleConversationIdExist(UUID senderId, UUID receiverId);
}
