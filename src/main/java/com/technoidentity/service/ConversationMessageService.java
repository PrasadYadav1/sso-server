package com.technoidentity.service;

import com.technoidentity.dto.ConversationMessageDto;
import com.technoidentity.dto.ConversationMessageResponse;
import com.technoidentity.entity.ConversationMessage;

import java.util.List;
import java.util.UUID;

public interface ConversationMessageService {
ConversationMessage add(ConversationMessageDto conversationMessageDto, UUID userId);
    void update(ConversationMessageDto conversationMessageDto,UUID id, UUID userId);

    List<ConversationMessageResponse> findByConversationId(UUID conversationId);

}
