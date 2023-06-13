package com.technoidentity.service;

import com.technoidentity.dto.*;
import com.technoidentity.entity.ConversationMessage;

import com.technoidentity.entity.ConversationParticipant;
import com.technoidentity.entity.ConversationReadReceipt;
import com.technoidentity.enums.MessageStatus;
import com.technoidentity.repository.ConversationMessageRepo;
import com.technoidentity.repository.ConversationParticipantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("conversationMessageService")
public class ConversationMessageServiceImpl implements ConversationMessageService{

    @Autowired
    private ConversationMessageRepo conversationMessageRepo;

    @Autowired
    private ConversationParticipantRepo conversationParticipantRepo;

    @Override
    public ConversationMessage add(ConversationMessageDto conversationMessageDto, UUID userId) {
        List<ConversationParticipant> conversationParticipants = conversationParticipantRepo.findByConversationId(conversationMessageDto.getConversationId());

        List<ConversationParticipant> conversationParticipant = conversationParticipants.stream().filter(f -> f.getUserId().equals(userId)).collect(Collectors.toList());
        ConversationMessage conversationMessage = new ConversationMessage();
        if(conversationMessageDto.getParentMessageId() != null) {
            conversationMessage.setParentMessageId(conversationMessageDto.getParentMessageId());
        }
        conversationMessage.setConversationId(conversationMessageDto.getConversationId());
        conversationMessage.setSenderParticipantId(conversationParticipant.get(0).getId());
        conversationMessage.setBody(conversationMessageDto.getBody());
        conversationMessage.setMedia(conversationMessageDto.getMedia());
        conversationMessage.setConversationDate(conversationMessageDto.getConversationDate());
        conversationMessage.setCreatedBy(userId);
        conversationMessage.setUpdatedBy(userId);
        conversationMessage.setCreatedAt(new Date());
        conversationMessage.setUpdatedAt(new Date());
        conversationMessage.setStatus(1);
        List<ConversationReadReceipt> conversationReadReceipts = conversationParticipants
                .stream().filter(c -> !c.getUserId().equals(userId))
                .map(r -> {
                    ConversationReadReceipt conversationReadReceipt = new ConversationReadReceipt();
                    conversationReadReceipt.setReceiverParticipantId(r.getId());
                    conversationReadReceipt.setMessageStatus(MessageStatus.Send);
                    conversationReadReceipt.setCreatedBy(userId);
                    conversationReadReceipt.setUpdatedBy(userId);
                    conversationReadReceipt.setCreatedAt(new Date());
                    conversationReadReceipt.setUpdatedAt(new Date());
                    conversationReadReceipt.setStatus(1);
                    return  conversationReadReceipt;
                }).collect(Collectors.toList());

        conversationMessage.setConversationReadReceipts(conversationReadReceipts);
        return conversationMessageRepo.save(conversationMessage);
    }

    @Override
    public void update(ConversationMessageDto conversationMessageDto, UUID id, UUID userId) {
        try {
            ConversationMessage conversationMessage = conversationMessageRepo.getById(id);
            if(conversationMessageDto.getParentMessageId() != null) {
                conversationMessage.setParentMessageId(conversationMessageDto.getParentMessageId());
            }
            if(conversationMessageDto.getConversationId() != null) {
                conversationMessage.setConversationId(conversationMessageDto.getConversationId());
            }

            if(conversationMessageDto.getBody() != null) {
                conversationMessage.setBody(conversationMessageDto.getBody());
            }
            if(conversationMessageDto.getMedia().size() != 0) {
                conversationMessage.setMedia(conversationMessageDto.getMedia());
            }
            if(conversationMessageDto.getConversationDate() != null) {
                conversationMessage.setConversationDate(conversationMessageDto.getConversationDate());
            }
            conversationMessage.setUpdatedBy(userId);
            conversationMessage.setUpdatedAt(new Date());

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ConversationMessageResponse> findByConversationId(UUID conversationId) {
        List<ConversationMessage> conversationMessages = conversationMessageRepo.findByConversationId(conversationId);
        List<ConversationMessageResponse> conversationMessageResponses = conversationMessages.stream().map(c -> {
            ConversationMessageResponse conversationMessageResponse = new ConversationMessageResponse();
            conversationMessageResponse.setId(c.getId());
            conversationMessageResponse.setParentMessageId(c.getParentMessageId());
            conversationMessageResponse.setConversationId(c.getConversationId());
            conversationMessageResponse.setSenderParticipantId(c.getSenderParticipantId());

            ConversationParticipantResponse conversationParticipantResponse = new ConversationParticipantResponse();
            conversationParticipantResponse.setId(c.getSenderParticipantId());
            conversationParticipantResponse.setConversationId(c.getSenderParticipant().getConversationId());
            conversationParticipantResponse.setUserId(c.getSenderParticipant().getUserId());
            UserResponseDto userDto = new UserResponseDto();
            userDto.setId(c.getSenderParticipant().getUserId());
            userDto.setEmail(c.getSenderParticipant().getUser().getEmail());
            userDto.setFirstName(c.getSenderParticipant().getUser().getFirstName());
            userDto.setMiddleName(c.getSenderParticipant().getUser().getMiddleName());
            userDto.setLastName(c.getSenderParticipant().getUser().getLastName());
            userDto.setGender(c.getSenderParticipant().getUser().getGender());
            userDto.setProfileImage(c.getSenderParticipant().getUser().getProfileImage());
            userDto.setDateOfBirth(c.getSenderParticipant().getUser().getDateOfBirth());
            userDto.setMobileNumber(c.getSenderParticipant().getUser().getMobileNumber());
            userDto.setUserStatus(c.getSenderParticipant().getUser().getUserStatus());
            conversationParticipantResponse.setUser(userDto);

            conversationMessageResponse.setSenderParticipant(conversationParticipantResponse);
            conversationMessageResponse.setBody(c.getBody());
            conversationMessageResponse.setMedia(c.getMedia());
            conversationMessageResponse.setConversationDate(c.getConversationDate());
            List<ConversationReadReceiptResponse> conversationReadReceiptResponses = c.getConversationReadReceipts().stream()
                    .map(r -> {
                        ConversationReadReceiptResponse conversationReadReceiptResponse = new ConversationReadReceiptResponse();
                        conversationReadReceiptResponse.setId(r.getId());
                        conversationReadReceiptResponse.setMessageId(r.getMessageId());
                        conversationReadReceiptResponse.setMessageStatus(r.getMessageStatus());
                        conversationReadReceiptResponse.setDescription(r.getDescription());
                        conversationReadReceiptResponse.setReceiverParticipantId(r.getReceiverParticipantId());

                        ConversationParticipantResponse conversationParticipantResponse1 = new ConversationParticipantResponse();
                        conversationParticipantResponse1.setId(r.getReceiverParticipantId());
                        conversationParticipantResponse1.setConversationId(r.getReceiverParticipant().getConversationId());
                        conversationParticipantResponse1.setUserId(r.getReceiverParticipant().getUserId());
                        UserResponseDto userDto1 = new UserResponseDto();
                        userDto1.setId(r.getReceiverParticipant().getUserId());
                        userDto1.setEmail(r.getReceiverParticipant().getUser().getEmail());
                        userDto1.setFirstName(r.getReceiverParticipant().getUser().getFirstName());
                        userDto1.setMiddleName(r.getReceiverParticipant().getUser().getMiddleName());
                        userDto1.setLastName(r.getReceiverParticipant().getUser().getLastName());
                        userDto1.setGender(r.getReceiverParticipant().getUser().getGender());
                        userDto1.setProfileImage(r.getReceiverParticipant().getUser().getProfileImage());
                        userDto1.setDateOfBirth(r.getReceiverParticipant().getUser().getDateOfBirth());
                        userDto1.setMobileNumber(r.getReceiverParticipant().getUser().getMobileNumber());
                        userDto1.setUserStatus(r.getReceiverParticipant().getUser().getUserStatus());
                        conversationParticipantResponse1.setUser(userDto1);
                        conversationReadReceiptResponse.setReceiverParticipant(conversationParticipantResponse1);
                        return conversationReadReceiptResponse;
                    }).collect(Collectors.toList());


            conversationMessageResponse.setConversationReadReceipts(conversationReadReceiptResponses);
            conversationMessageResponse.setCreatedAt(c.getCreatedAt());
            conversationMessageResponse.setUpdatedAt(c.getUpdatedAt());
            conversationMessageResponse.setStatus(c.getStatus());
            return conversationMessageResponse;
        }).collect(Collectors.toList());
        return conversationMessageResponses;
    }
}
