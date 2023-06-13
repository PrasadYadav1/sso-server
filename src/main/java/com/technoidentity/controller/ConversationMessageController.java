package com.technoidentity.controller;

import com.technoidentity.dto.ChatUserDto;
import com.technoidentity.dto.ConversationDto;
import com.technoidentity.dto.ConversationMessageDto;
import com.technoidentity.dto.ConversationMessageResponse;
import com.technoidentity.entity.Conversation;
import com.technoidentity.entity.ConversationMessage;
import com.technoidentity.enums.ConversationType;
import com.technoidentity.service.ConversationMessageService;
import com.technoidentity.service.UserPrincipal;
import com.technoidentity.util.CommonResponse;
import com.technoidentity.util.DateFormats;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/conversation-messages")
@Api(tags = "ConversationMessages")
public class ConversationMessageController {

    @Autowired
    private ConversationMessageService conversationMessageService;

    SimpleDateFormat sm = new DateFormats().DATE_TIME_FORMAT;
   /* @MessageMapping("/chat")
    public ResponseEntity<?> postConversationMessage(@Payload ConversationMessageDto conversationMessageDto) {
          System.out.println("welcome to post conversation");
       // UserPrincipal userDetails =
       //         (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ConversationMessage conversation = conversationMessageService.add(conversationMessageDto, conversationMessageDto.getUserId());
        return new ResponseEntity(new CommonResponse(conversation.getId(),
                sm.format(new Date()), HttpServletResponse.SC_OK,
                "", "conversation message send successfully", "/api/conversation-messages"), HttpStatus.OK);

    }
*/
    @GetMapping("/conversations")
    public ResponseEntity<? >  findAllByConversationId(@RequestParam(required = false) UUID conversationId) {
        UserPrincipal userDetails =
                (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ConversationMessageResponse> conversationMessages = conversationMessageService.findByConversationId(conversationId);
        return new ResponseEntity<>(conversationMessages, HttpStatus.OK);
    }
}


