package com.technoidentity.controller;

import com.technoidentity.dto.ConversationMessageResponse;
import com.technoidentity.entity.Role;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import com.technoidentity.security.CustomUserDetailsService;
import com.technoidentity.security.TokenProvider;
import com.technoidentity.security.oauth2.CustomOAuth2UserService;
import com.technoidentity.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.technoidentity.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.technoidentity.service.ConversationMessageService;
import com.technoidentity.service.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(ConversationMessageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ConversationMessageControllerTests {

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
    private ConversationMessageService conversationMessageService;

    @Test
    public void test_findAllByConversationId() throws Exception {
        UUID conversationId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        ConversationMessageResponse conversationMessageResponse = ConversationMessageResponse.builder()
                .id(id)
                .conversationId(conversationId)
                .description("Conversation Message")
                .media(List.of("Image.png"))
                .createdAt(new Date())
                .updatedAt(new Date())
                .status(1)
                .build();

        Role role = Role.builder().id(UUID.randomUUID()).name("Raj").description("write-role").build();
        User user = User.builder().id(UUID.randomUUID()).firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345").provider(AuthProvider.google)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("google123")
                .emailVerified(false).gender(Gender.Male).build();
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, "Test@123");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(conversationMessageService.findByConversationId(any(UUID.class))).thenReturn(List.of(conversationMessageResponse));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/conversation-messages/conversations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("conversationId", String.valueOf(conversationId)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Conversation Message"));

    }
}
