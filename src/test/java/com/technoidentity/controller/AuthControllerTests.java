
package com.technoidentity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoidentity.dto.SignIn;
import com.technoidentity.dto.SignUp;
import com.technoidentity.entity.Role;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import com.technoidentity.exception.BadRequestException;
import com.technoidentity.security.CustomUserDetailsService;
import com.technoidentity.security.TokenProvider;
import com.technoidentity.security.oauth2.CustomOAuth2UserService;
import com.technoidentity.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.technoidentity.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.technoidentity.service.UserPrincipal;
import com.technoidentity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
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
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserService userService;
    private SignIn signIn;
    private String token;
    private String URI;
    private Role role;
    private User user;
    private UserPrincipal userPrincipal;
    private SignUp signup;

    @BeforeEach
    public void init(){
        role = Role.builder().id(UUID.randomUUID()).name("Raj").description("write-role").build();
        user = User.builder().id(UUID.randomUUID()).firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345").provider(AuthProvider.google)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("google123")
                .emailVerified(false).gender(Gender.Male).build();
        signIn = SignIn.builder().email("abc@gmail.com").password("12345").build();
        signup = SignUp.builder().firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345").confirmPassword("12345")
                .email("abc@gmail.com").gender(Gender.Male).build();
        token = "mocked-token";
        userPrincipal = UserPrincipal.create(user);
        URI ="http://localhost/api/users/me";
    }


    @Test
    public void test_signIn() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenProvider.createToken(authentication)).thenReturn(token);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signIn)));

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tokenType").value("Bearer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email").value(signIn.getEmail()));
    }

    @Test
    public void test_signUp() throws Exception {
        when(userService.existsByEmail(anyString())).thenReturn(false);
        when(userService.signUp(any(SignUp.class))).thenReturn(user);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signup)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", URI))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User registered successfully"));

    }

    @Test
    public void test_signUp_Exception() throws Exception {

        when(userService.existsByEmail(anyString())).thenReturn(true);
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signup)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email address already in use."));
    }


}
