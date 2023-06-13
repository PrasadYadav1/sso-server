package com.technoidentity.service;

import com.technoidentity.repository.ConversationParticipantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("conversationParticipantService")
public class ConversationParticipantServiceImpl implements ConversationParticipantService{

    @Autowired
    private ConversationParticipantRepo conversationParticipantRepo;
    @Override
    public UUID findBySingleConversationIdExist(UUID senderId, UUID receiverId) {
        UUID conversationId= conversationParticipantRepo.findBySingleConversationIdExist(senderId,receiverId);
        return conversationId;
    }
}
