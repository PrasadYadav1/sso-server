package com.technoidentity.service;

import com.technoidentity.dto.ConversationDto;
import com.technoidentity.entity.Conversation;

import java.util.UUID;

public interface ConversationService {
    Conversation add(ConversationDto conversationDto, UUID userId);

    void update(ConversationDto conversationDto, UUID id, UUID userId);

    Conversation getByName(String name);
}
