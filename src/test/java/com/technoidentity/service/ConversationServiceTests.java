package com.technoidentity.service;

import com.technoidentity.dto.ConversationDto;
import com.technoidentity.dto.ParticipantDto;
import com.technoidentity.entity.Conversation;
import com.technoidentity.enums.ConversationType;
import com.technoidentity.repository.ConversationRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConversationServiceTests {


    @Mock
    private ConversationRepo conversationRepo;
    @InjectMocks
    private ConversationServiceImpl conversationService;
    private ConversationDto conversationDto;
    private ParticipantDto participantDto;
    private UUID userID;
    private Conversation conversation;

    @BeforeEach
    public void init(){
        userID = UUID.randomUUID();
        participantDto = ParticipantDto.builder()
                .userId(userID)
                .id(UUID.randomUUID())
                .status(1)
                .build();
        conversationDto = ConversationDto.builder()
                .conversationType(ConversationType.Single)
                .name("First-Conversation")
                .participants(List.of(participantDto))
                .build();
        conversation = Conversation.builder()
                .conversationType(ConversationType.Single)
                .id(UUID.randomUUID())
                .name("First-Conversation")
                .build();
    }

    @Test
    public void test_add(){
        when(conversationRepo.save(any())).thenReturn(conversation);

        Conversation expectedConversation = conversationService.add(conversationDto, userID);
        Assertions.assertThat(expectedConversation).isNotNull();
        Assertions.assertThat(conversation.getId()).isEqualTo(expectedConversation.getId());
        Assertions.assertThat(ConversationType.Single).isEqualTo(expectedConversation.getConversationType());
        Assertions.assertThat(conversation.getName()).isEqualTo(expectedConversation.getName());
    }

    @Test
    public void test_update(){
        ConversationDto updatedConversationDto = ConversationDto.builder()
                .conversationType(ConversationType.Single)
                .name("Second-Conversation")
                .participants(List.of(participantDto))
                .build();
        when(conversationRepo.getById(any(UUID.class))).thenReturn(conversation);
        UUID id = UUID.randomUUID();
        conversationService.update(updatedConversationDto, id, userID);
        verify(conversationRepo, times(1)).getById(id);

        //null case
        ParticipantDto participantDto1 = ParticipantDto.builder()
                .userId(userID)
                .status(1)
                .build();
        updatedConversationDto.setParticipants(List.of(participantDto1));
        conversationService.update(updatedConversationDto, id, userID);
        verify(conversationRepo, times(2)).getById(id);

    }

    @Test
    public void test_getByName(){
        when(conversationRepo.findByNameContainingIgnoreCase(anyString())).thenReturn(conversation);
        Conversation expectedConversation = conversationService.getByName("First");
        Assertions.assertThat(expectedConversation).isNotNull();
        Assertions.assertThat(conversation.getName()).isEqualTo(expectedConversation.getName());

    }
}
