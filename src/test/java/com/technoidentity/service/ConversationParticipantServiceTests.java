package com.technoidentity.service;

import com.technoidentity.repository.ConversationParticipantRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConversationParticipantServiceTests {


    @Mock
    private ConversationParticipantRepo conversationParticipantRepo;

    @InjectMocks
    private ConversationParticipantServiceImpl conversationParticipantService;


    @Test
    public void test_findBySingleConversationIdExist(){
        UUID conversationId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        when(conversationParticipantRepo.findBySingleConversationIdExist(any(UUID.class), any(UUID.class))).thenReturn(conversationId);
        UUID expectedUUID = conversationParticipantService.findBySingleConversationIdExist(senderId, receiverId);
        Assertions.assertThat(conversationId).isEqualTo(expectedUUID);
        Assertions.assertThat(expectedUUID).isNotNull();
    }
}
