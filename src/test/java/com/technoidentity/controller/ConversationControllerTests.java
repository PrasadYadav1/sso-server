package com.technoidentity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoidentity.dto.ConversationDto;
import com.technoidentity.dto.ParticipantDto;
import com.technoidentity.entity.Conversation;
import com.technoidentity.entity.ConversationParticipant;
import com.technoidentity.entity.Role;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.ConversationType;
import com.technoidentity.enums.Gender;
import com.technoidentity.security.CustomUserDetailsService;
import com.technoidentity.security.TokenProvider;
import com.technoidentity.security.oauth2.CustomOAuth2UserService;
import com.technoidentity.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.technoidentity.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.technoidentity.service.ConversationParticipantService;
import com.technoidentity.service.ConversationService;
import com.technoidentity.service.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ConversationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ConversationControllerTests {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;
    @MockBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    @MockBean
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private ConversationService conversationService;
    @MockBean
    private ConversationParticipantService conversationParticipantService;
    private ConversationDto conversationSingleDto;
    private ConversationDto conversationGroupDto;
    private UUID userID;
    private User user;
    private Role role;
    private UserPrincipal userPrincipal;
    private ParticipantDto participantDto;

    @BeforeEach
    public void init(){
        userID = UUID.randomUUID();
        participantDto = ParticipantDto.builder()
                .userId(userID)
                .id(UUID.randomUUID())
                .status(1)
                .build();
        conversationSingleDto = ConversationDto.builder()
                .conversationType(ConversationType.Single)
                .name("Single-Conversation")
                .participants(List.of(participantDto))
                .build();
        conversationGroupDto = ConversationDto.builder()
                .conversationType(ConversationType.Group)
                .name("Group-Conversation")
                .participants(List.of(participantDto))
                .build();
        role = Role.builder().id(UUID.randomUUID()).name("Raj").description("write-role").build();
        user = User.builder().id(UUID.randomUUID()).firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345").provider(AuthProvider.google)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("google123")
                .emailVerified(false).gender(Gender.Male).build();
        userPrincipal = UserPrincipal.create(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, "Test@123");
        SecurityContextHolder.getContext().setAuthentication(authentication);


    }

    @Test
    public void test_postConversation_SingleInvalid() throws Exception {

        // Case when conversationID already exists
        when(conversationParticipantService.findBySingleConversationIdExist(any(UUID.class), any(UUID.class))).thenReturn(UUID.randomUUID());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/conversations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conversationSingleDto)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("all ready conversation created"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/api/conversations"));


    }

    @Test
    public void test_postConversation_Single_TwoParticipant() throws  Exception{
        ParticipantDto participantDto1 = ParticipantDto.builder()
                .userId(UUID.randomUUID())
                .id(UUID.randomUUID())
                .status(1)
                .build();
        conversationSingleDto.setParticipants(List.of(participantDto, participantDto1));

        when(conversationParticipantService.findBySingleConversationIdExist(any(UUID.class), any(UUID.class))).thenReturn(UUID.randomUUID());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/conversations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conversationSingleDto)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("only one participant accepted"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/api/conversations"));

    }

    @Test
    public void test_postConversation_Valid() throws Exception {

        Conversation conversation = Conversation.builder()
                .conversationType(ConversationType.Single)
                .id(UUID.randomUUID())
                .name("Valid-Conversation")
                .build();

        when(conversationParticipantService.findBySingleConversationIdExist(any(UUID.class), any(UUID.class))).thenReturn(null);
        when(conversationService.add(any(ConversationDto.class), any(UUID.class))).thenReturn(conversation);

        ResultActions validResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/conversations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conversationSingleDto)));
        validResponse.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("conversation added successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/api/conversations"));

    }

    @Test
    public void test_postConversation_Group() throws Exception{
        conversationGroupDto.setName(null);

        ResultActions validResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/conversations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conversationGroupDto)));

        validResponse.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("name is mandatory "))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/api/conversations"));

    }

    @Test
    public void test_postConversation_Group_NameExist() throws Exception{
        Conversation conversation = Conversation.builder()
                .conversationType(ConversationType.Group)
                .id(UUID.randomUUID())
                .name("Already-existed-Conversation")
                .build();
        when(conversationService.getByName(anyString())).thenReturn(conversation);
        ResultActions validResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/conversations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conversationGroupDto)));

        validResponse.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("name already exist"));

    }

}
