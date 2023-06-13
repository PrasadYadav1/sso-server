
package com.technoidentity.controller;


import antlr.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoidentity.dto.ChatUserDto;
import com.technoidentity.dto.UpdateUserDto;
import com.technoidentity.dto.UserDto;
import com.technoidentity.dto.UserResponseDto;
import com.technoidentity.entity.Role;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import com.technoidentity.enums.UserStatus;
import com.technoidentity.security.CustomUserDetailsService;
import com.technoidentity.security.TokenProvider;
import com.technoidentity.security.oauth2.CustomOAuth2UserService;
import com.technoidentity.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.technoidentity.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.technoidentity.service.FileStorageService;
import com.technoidentity.service.ImageStorageService;
import com.technoidentity.service.UserPrincipal;
import com.technoidentity.service.UserService;
import com.technoidentity.util.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.xml.transform.Result;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
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
    private ImageStorageService imageStorageService;
    @MockBean
    private FileStorageService fileStorageService;
    @MockBean
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private User user;
    private Role role;
    private UserPrincipal userPrincipal;
    private UserResponseDto userResponseDto;
    private UpdateUserDto updateUserDto;


    @BeforeEach
    public void init(){
        role = Role.builder().id(UUID.randomUUID()).name("Raj").description("write-role").build();
        user = User.builder().id(UUID.randomUUID()).firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345").provider(AuthProvider.google)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("google123")
                .emailVerified(false).gender(Gender.Male).build();
        userPrincipal = UserPrincipal.create(user);
        userResponseDto = new UserResponseDto(user.getId(),user.getFirstName(),user.getMiddleName(),user.getLastName(),
                user.getEmail(),user.getProfileImage(),user.getGender(),user.getMobileNumber(),user.getDateOfBirth(),
                user.getUserStatus());
        updateUserDto = new UpdateUserDto(user.getFirstName(),user.getMiddleName(),user.getLastName(),
                user.getProfileImage(), user.getGender(), user.getMobileNumber(), user.getDateOfBirth());

    }

    @Test
    public void test_getCurrentUser() throws Exception {
        when(userService.getById(any())).thenReturn(user);

        UserDto userDto = userController.getCurrentUser(userPrincipal);
        assertThat(userDto).isNotNull();
        assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDto.getMiddleName()).isEqualTo(user.getMiddleName());

    }

    @Test
    public void test_getUserDetails() throws Exception {
        when(userService.getUserDetails(any())).thenReturn(userResponseDto);
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.middleName").value(user.getMiddleName()));

    }

    @Test
    public void test_findBeName() throws Exception {
        when(userService.findByNameContainingIgnoreCase(any())).thenReturn(List.of(userResponseDto));
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/all-users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "aj"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].id").value(user.getId().toString()))
                .andExpect(jsonPath("$[0].firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$[0].middleName").value(user.getMiddleName()));

    }
    @Test
    public void test_findByName_invalid() throws Exception {
        when(userService.findByNameContainingIgnoreCase(any())).thenReturn(List.of());
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/all-users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "aj"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void test_updateUserStatus() throws Exception {
        doNothing().when(userService).updateStatus(any(),any());
        ResultActions response = mockMvc.perform(put("/api/users/user-status/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .param("status", "ACTIVE"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("User status updated successfully"))
                .andExpect(jsonPath("$.success").value("true"));
    }

    @Test
    public void test_updateUser() throws Exception {
        when(userService.updateUser(any(),any())).thenReturn(userResponseDto);
        ResultActions response = mockMvc.perform(put("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDto)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.userInfoDto.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.userInfoDto.middleName").value(user.getMiddleName()));
    }

    @Test
    public void test_usersPagination() throws Exception {
        UserResponseDto userResponseDto2 = new UserResponseDto(UUID.randomUUID(),"Rahim","khan","abdul",
                "ab@email.com","http://fksl.jpg",Gender.Male,"9823892883",user.getDateOfBirth(),
                user.getUserStatus());

        when(userService.findByNameAndStatus(any(), anyString(), any())).thenReturn(new Pagination(List.of(userResponseDto, userResponseDto2), 1));
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                .param("pageIndex", "0")
                .param("pageSize", "5")
                .param("search", "ram")
                .param("status", "0")
                .param("sortBy", "firstName")
                .param("sortOrder", "DESC")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data[0].firstName").value("Raj"))
                .andExpect(jsonPath("$.data[0].lastName").value("Vera"))
                .andExpect(jsonPath("$.data[1].profileImage").value("http://fksl.jpg"))
                .andExpect(jsonPath("$.data[1].gender").value("Male"));
    }

    @Test
    public void test_findAllChatUsers() throws Exception {
        UUID conversationID = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ChatUserDto chatUserDto = ChatUserDto.builder().id(userId).firstName("Ram")
                .middleName("singh").lastName("Panwar").email("abc@gmail.com")
                .profileImage("image.png").gender(Gender.Male).mobileNumber("923794273")
                .dateOfBirth(new Date(2000, 7, 8)).userStatus(UserStatus.BUSY)
                .conversationId(conversationID).unreadMessageCount(2).build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, "Test@123");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userService.findByNameOrEmailContainingIgnoreCase(any(UUID.class), anyString())).thenReturn(List.of(chatUserDto));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/chat-users")
                .param("name", "Ram"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(userId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("abc@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].unreadMessageCount").value(2));


    }

    @Test
    public void test_profileImage() throws Exception {
        String path = "http://localhost:9090/image/photo.png";
        when(imageStorageService.save(any(UUID.class), any())).thenReturn(path);
        UUID id = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("image", "photo.png", MediaType.IMAGE_PNG_VALUE, "Unit-test-case content".getBytes());
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users/profile-image/{id}", id)
                        .file(file));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Image uploaded successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uri").value(path));
    }

    @Test
    public void test_downloadImage() throws Exception {

        Resource resource = new ByteArrayResource("Unit test resource".getBytes());
        when(imageStorageService.downloadImage(any(UUID.class))).thenReturn(resource);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/profile-image/{id}", UUID.randomUUID()));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().bytes(resource.getInputStream().readAllBytes()));

    }

    @Test
    public void test_deleteImage() throws Exception {

        doNothing().when(imageStorageService).delete(any(UUID.class));
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/profile-image/{id}", UUID.randomUUID()));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Image deleted successfully"));

    }


}

