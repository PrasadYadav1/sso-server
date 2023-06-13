package com.technoidentity.controller;

import com.technoidentity.dto.*;
import com.technoidentity.entity.ConversationMessage;
import com.technoidentity.service.ConversationMessageService;
import com.technoidentity.service.UserPrincipal;
import com.technoidentity.util.DateFormats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Controller
public class ChatController {

    @Autowired
    private ConversationMessageService conversationMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    SimpleDateFormat sm = new DateFormats().DATE_TIME_FORMAT;
  /*  @MessageMapping("/chat")
    public ResponseEntity<?> postConversationMessage(@Payload ConversationMessageDto conversationMessageDto) {
          System.out.println("welcome to post conversation");
       // UserPrincipal userDetails =
       //         (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ConversationMessage conversation = conversationMessageService.add(conversationMessageDto, conversationMessageDto.getUserId());
        return new ResponseEntity(new CommonResponse(conversation.getId(),
                sm.format(new Date()), HttpServletResponse.SC_OK,
                "", "conversation message send successfully", "/api/conversation-messages"), HttpStatus.OK);

    }*/

    @MessageMapping("/chat")
    public void processMessage(@Payload ConversationMessageDto conversationMessageDto) {
        ConversationMessage conversationMessage = conversationMessageService.add(conversationMessageDto, conversationMessageDto.getUserId());
        SenderParticipantResponse senderParticipantResponse = new SenderParticipantResponse();
        senderParticipantResponse.setId(conversationMessage.getSenderParticipantId());
        senderParticipantResponse.setUserId(conversationMessage.getSenderParticipant().getUserId());
        SenderParticipantUserResponse senderParticipantUserResponse = new SenderParticipantUserResponse();
        senderParticipantUserResponse.setId(conversationMessage.getSenderParticipant().getUserId());
        senderParticipantUserResponse.setFirstName(conversationMessage.getSenderParticipant().getUser().getFirstName());
        senderParticipantUserResponse.setMiddleName(conversationMessage.getSenderParticipant().getUser().getMiddleName());
        senderParticipantUserResponse.setLastName(conversationMessage.getSenderParticipant().getUser().getLastName());
        senderParticipantResponse.setUser(senderParticipantUserResponse);
        messagingTemplate.convertAndSendToUser(
                conversationMessageDto.getUserId().toString(),"/queue/messages",
                new ChatNotificationDto(
                        conversationMessage.getId(),
                        conversationMessage.getConversationId(),
                        conversationMessage.getSenderParticipantId(),
                        senderParticipantResponse,
                        conversationMessage.getBody(),
                        conversationMessage.getMedia(),
                        conversationMessage.getParentMessageId(),
                        conversationMessage.getDescription(),
                        conversationMessage.getConversationDate()
                        ));
    }


}


