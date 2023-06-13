package com.technoidentity.controller;

import com.technoidentity.dto.ConversationDto;
import com.technoidentity.entity.Conversation;
import com.technoidentity.enums.ConversationType;
import com.technoidentity.service.ConversationParticipantService;
import com.technoidentity.service.ConversationService;
import com.technoidentity.service.UserPrincipal;
import com.technoidentity.util.CommonResponse;
import com.technoidentity.util.DateFormats;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping("/api/conversations")
@Api(tags = "Conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ConversationParticipantService conversationParticipantService;

    SimpleDateFormat sm = new DateFormats().DATE_TIME_FORMAT;
    @PostMapping
    public ResponseEntity<?> postConversation(@Valid @RequestBody ConversationDto conversationDto) {

        if(ConversationType.Group == conversationDto.getConversationType()) {
           if(conversationDto.getName() == null || conversationDto.getName().isEmpty()) {
               return new ResponseEntity(new CommonResponse(null,
                       sm.format(new Date()), HttpServletResponse.SC_BAD_REQUEST,
                       "", "name is mandatory ", "/api/conversations"), HttpStatus.BAD_REQUEST);
           }
            Conversation c = conversationService.getByName(conversationDto.getName());
            if (c != null) {
                return new ResponseEntity<>("name already exist",HttpStatus.BAD_REQUEST);
            }
        }
        UserPrincipal userDetails =
                (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(ConversationType.Single == conversationDto.getConversationType()){
            if(conversationDto.getParticipants().size() != 1) {
                return new ResponseEntity(new CommonResponse(null,
                        sm.format(new Date()), HttpServletResponse.SC_BAD_REQUEST,
                        "", "only one participant accepted", "/api/conversations"), HttpStatus.BAD_REQUEST);

            }
            UUID conversationId = conversationParticipantService.findBySingleConversationIdExist(userDetails.getId(),conversationDto.getParticipants().get(0).getUserId());
            if (conversationId != null) {
                return new ResponseEntity(new CommonResponse(conversationId,
                        sm.format(new Date()), HttpServletResponse.SC_BAD_REQUEST,
                        "", "all ready conversation created", "/api/conversations"), HttpStatus.BAD_REQUEST);

            }
        }
        Conversation conversation = conversationService.add(conversationDto, userDetails.getId());
        return new ResponseEntity(new CommonResponse(conversation.getId(),
                sm.format(new Date()), HttpServletResponse.SC_OK,
                "", "conversation added successfully", "/api/conversations"), HttpStatus.OK);

    }
}
