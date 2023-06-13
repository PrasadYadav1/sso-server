package com.technoidentity.service;

import com.technoidentity.dto.ConversationMessageDto;
import com.technoidentity.dto.ConversationMessageResponse;
import com.technoidentity.entity.ConversationMessage;
import com.technoidentity.entity.ConversationParticipant;
import com.technoidentity.entity.ConversationReadReceipt;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import com.technoidentity.enums.MessageStatus;
import com.technoidentity.repository.ConversationMessageRepo;
import com.technoidentity.repository.ConversationParticipantRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConversationMessageServiceTests {

    @Mock
    private ConversationMessageRepo conversationMessageRepo;
    @Mock
    private ConversationParticipantRepo conversationParticipantRepo;
    @InjectMocks
    private ConversationMessageServiceImpl conversationMessageService;
    private ConversationMessageDto conversationMessageDto;
    private UUID userId;
    private UUID conversationId;
    private ConversationMessage conversationMessage;
    private ConversationParticipant conversationParticipant1;
    private ConversationParticipant conversationParticipant2;
    private ConversationReadReceipt conversationReadReceipt;

    @BeforeEach
    public void init(){
        userId = UUID.randomUUID();
        conversationId = UUID.randomUUID();
        conversationReadReceipt = ConversationReadReceipt.builder()
                .id(UUID.randomUUID())
                .messageId(UUID.randomUUID())
                .receiverParticipantId(UUID.randomUUID())
                .messageStatus(MessageStatus.Send)
                .build();
        conversationMessage = ConversationMessage.builder()
                .id(UUID.randomUUID())
                .conversationId(conversationId)
                .senderParticipantId(UUID.randomUUID())
                .body("Hi, How are you?")
                .media(List.of("image.png"))
                .conversationReadReceipts(List.of(conversationReadReceipt))
                .build();
        conversationMessageDto = ConversationMessageDto.builder()
                .userId(userId)
                .conversationId(conversationId)
                .media(List.of("image.png"))
                .body("Hi, How are you?")
                .build();
        conversationParticipant1 = ConversationParticipant.builder()
                .id(UUID.randomUUID())
                .conversationId(conversationId)
                .userId(userId)
                .conversationMessages(List.of(conversationMessage))
                .conversationReadReceipts(List.of(conversationReadReceipt))
                .build();

    }

    @Test
    public void test_add(){
        conversationParticipant2 = ConversationParticipant.builder()
                .id(UUID.randomUUID())
                .conversationId(conversationId)
                .userId(UUID.randomUUID())
                .conversationMessages(List.of(conversationMessage))
                .conversationReadReceipts(List.of(conversationReadReceipt))
                .build();

        when(conversationParticipantRepo.findByConversationId(any(UUID.class))).thenReturn(List.of(conversationParticipant1, conversationParticipant2));
        when(conversationMessageRepo.save(any(ConversationMessage.class))).thenReturn(conversationMessage);

        ConversationMessage expectedMessage = conversationMessageService.add(conversationMessageDto, userId);

        Assertions.assertThat(expectedMessage).isNotNull();
        Assertions.assertThat("Hi, How are you?").isEqualTo(expectedMessage.getBody());
        Assertions.assertThat(conversationReadReceipt.getMessageId()).isEqualTo(expectedMessage.getConversationReadReceipts().get(0).getMessageId());
        Assertions.assertThat(conversationMessage.getMedia()).isEqualTo(expectedMessage.getMedia());

    }

    @Test
    public void test_update(){
        UUID id = UUID.randomUUID();
        ConversationMessageDto conversationMessageDto1 = ConversationMessageDto.builder()
                .userId(userId)
                .conversationId(conversationId)
                .parentMessageId(UUID.randomUUID())
                .media(List.of("random_image.png"))
                .body("Hi, I'm fine")
                .build();
        when(conversationMessageRepo.getById(any(UUID.class))).thenReturn(conversationMessage);
        conversationMessageService.update(conversationMessageDto1, id, userId);

        verify(conversationMessageRepo, times(1)).getById(id);
        Assertions.assertThat(List.of("random_image.png")).isEqualTo(conversationMessage.getMedia());
        Assertions.assertThat("Hi, I'm fine").isEqualTo(conversationMessage.getBody());

    }

    @Test
    public void test_findByConversationId(){
        User user = User.builder().id(UUID.randomUUID()).firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345").provider(AuthProvider.google)
                .email("abc@gmail.com").providerId("google123")
                .emailVerified(false).gender(Gender.Male).build();
        conversationParticipant2 = ConversationParticipant.builder()
                .id(UUID.randomUUID())
                .conversationId(conversationId)
                .userId(UUID.randomUUID())
                .conversationMessages(List.of(conversationMessage))
                .conversationReadReceipts(List.of(conversationReadReceipt))
                .build();
        conversationParticipant1.setUser(user);
        conversationParticipant2.setUser(user);
        conversationReadReceipt.setDescription("Read receipt description");
        conversationReadReceipt.setReceiverParticipant(conversationParticipant2);
        conversationMessage.setParentMessageId(UUID.randomUUID());
        conversationMessage.setSenderParticipant(conversationParticipant1);

        when(conversationMessageRepo.findByConversationId(any(UUID.class))).thenReturn(List.of(conversationMessage));
        List<ConversationMessageResponse> conversationMessageResponseList = conversationMessageService.findByConversationId(UUID.randomUUID());

        Assertions.assertThat(conversationMessageResponseList).isNotNull();
        Assertions.assertThat("Hi, How are you?").isEqualTo(conversationMessageResponseList.get(0).getBody());
        Assertions.assertThat(List.of("image.png")).isEqualTo(conversationMessageResponseList.get(0).getMedia());
        Assertions.assertThat(conversationMessage.getParentMessageId()).isEqualTo(conversationMessageResponseList.get(0).getParentMessageId());
        Assertions.assertThat(conversationMessage.getSenderParticipantId()).isEqualTo(conversationMessageResponseList.get(0).getSenderParticipantId());
        Assertions.assertThat("abc@gmail.com").isEqualTo(conversationMessageResponseList.get(0).getSenderParticipant().getUser().getEmail());

    }

}
