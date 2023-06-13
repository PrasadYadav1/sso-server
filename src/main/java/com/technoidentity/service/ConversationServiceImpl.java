package com.technoidentity.service;

import com.technoidentity.dto.ConversationDto;
import com.technoidentity.entity.Conversation;
import com.technoidentity.entity.ConversationParticipant;
import com.technoidentity.repository.ConversationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("conversationService")
public class ConversationServiceImpl implements ConversationService{

    @Autowired
    private ConversationRepo conversationRepo;

    @Override
    public Conversation add(ConversationDto conversationDto, UUID userId) {
        Conversation conversation = new Conversation();
        conversation.setConversationType(conversationDto.getConversationType());
        conversation.setName(conversationDto.getName());
        conversation.setCreatedBy(userId);
        conversation.setUpdatedBy(userId);
        conversation.setCreatedAt(new Date());
        conversation.setUpdatedAt(new Date());
        conversation.setStatus(1);
        if ((conversationDto.getParticipants() != null) && !conversationDto.getParticipants().isEmpty()) {
            List<ConversationParticipant> participants = conversationDto.getParticipants().stream().map(p -> {
                ConversationParticipant participant = new ConversationParticipant();
                participant.setUserId(p.getUserId());
                participant.setCreatedBy(userId);
                participant.setUpdatedBy(userId);
                participant.setCreatedAt(new Date());
                participant.setUpdatedAt(new Date());
                participant.setStatus(1);
                return participant;
            }).collect(Collectors.toList());
            ConversationParticipant participant1 = new ConversationParticipant();
            participant1.setUserId(userId);
            participant1.setCreatedBy(userId);
            participant1.setUpdatedBy(userId);
            participant1.setCreatedAt(new Date());
            participant1.setUpdatedAt(new Date());
            participant1.setStatus(1);
             participants.add(participant1);
            conversation.setConversationParticipants(participants);
        }
        return conversationRepo.save(conversation);
    }

    @Override
    public void update(ConversationDto conversationDto, UUID id, UUID userId) {
        try {
            Conversation conversation = conversationRepo.getById(id);
           if(conversationDto.getName() != null){
               conversation.setName(conversationDto.getName());
           }
            conversation.setUpdatedBy(userId);
            conversation.setUpdatedAt(new Date());
            if ((conversationDto.getParticipants() != null) && !conversationDto.getParticipants().isEmpty()) {
                List<ConversationParticipant> participants = conversationDto.getParticipants().stream().map(p -> {
                    ConversationParticipant participant = new ConversationParticipant();
                    if(p.getId() != null){
                        participant.setId(p.getId());
                        participant.setUserId(p.getUserId());
                        participant.setUpdatedBy(userId);
                        participant.setUpdatedAt(new Date());
                        participant.setStatus(p.getStatus());
                    }
                    if(p.getId() == null) {
                        participant.setUserId(p.getUserId());
                        participant.setCreatedBy(userId);
                        participant.setUpdatedBy(userId);
                        participant.setCreatedAt(new Date());
                        participant.setUpdatedAt(new Date());
                        participant.setStatus(1);
                    }
                    return participant;
                }).collect(Collectors.toList());
                conversation.setConversationParticipants(participants);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Conversation getByName(String name) {
        Conversation conversation = conversationRepo.findByNameContainingIgnoreCase(name);
        return conversation;
    }
}
