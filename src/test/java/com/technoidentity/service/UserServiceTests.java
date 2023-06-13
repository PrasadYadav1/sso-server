package com.technoidentity.service;

import com.technoidentity.dto.ChatUserDto;
import com.technoidentity.dto.SignUp;
import com.technoidentity.dto.UpdateUserDto;
import com.technoidentity.dto.UserResponseDto;
import com.technoidentity.entity.*;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import com.technoidentity.enums.MessageStatus;
import com.technoidentity.repository.ConversationParticipantRepo;
import com.technoidentity.repository.RoleRepo;
import com.technoidentity.repository.UserRepo;

import com.technoidentity.util.Pagination;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.technoidentity.enums.UserStatus.ONLINE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepo userRepo;
    @Mock
    private RoleRepo roleRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ConversationParticipantRepo conversationParticipantRepo;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;
    private Role role;
    private UserDetails userDetails;
    private SignUp signup;
    private UpdateUserDto updateUserDto;

    @BeforeEach
    public void init(){
        role = Role.builder().id(UUID.randomUUID()).name("Raj").description("write-role").build();
        user = User.builder().id(UUID.randomUUID()).firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345").provider(AuthProvider.google)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("google123")
                .emailVerified(false).gender(Gender.Male).build();
        userDetails = UserPrincipal.create(user);
        signup = SignUp.builder().firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345")
                .email("abc@gmail.com").gender(Gender.Male).build();
        updateUserDto = new UpdateUserDto(user.getFirstName(),user.getMiddleName(),user.getLastName(),user.getProfileImage()
                ,user.getGender(), user.getMobileNumber(),null);

    }

    @Test
    public void test_loadUserByUsername(){
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        UserDetails expectedUserDetails = userService.loadUserByUsername("abc@gmail.com");
        assertThat(expectedUserDetails).isNotNull();
        assertThat(expectedUserDetails.getUsername()).isEqualTo(userDetails.getUsername());
        assertThat(expectedUserDetails.getAuthorities()).isEqualTo(userDetails.getAuthorities());
        assertThat(expectedUserDetails.getPassword()).isEqualTo("12345");
        assertThat(expectedUserDetails.isAccountNonExpired()).isTrue();

        //throws case
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("ram"));

    }

    @Test
    public void test_loadUserById(){
        when(userRepo.findById(any())).thenReturn(Optional.of(user));
        UserDetails expectedResult = userService.loadUserById(user.getId());
        assertThat(expectedResult).isNotNull();
        assertThat(expectedResult.getUsername()).isEqualTo(userDetails.getUsername());
        assertThat(expectedResult.getAuthorities()).isEqualTo(userDetails.getAuthorities());
        assertThat(expectedResult.isAccountNonLocked()).isTrue();
        assertThat(expectedResult.isEnabled()).isTrue();
        assertThat(expectedResult.isCredentialsNonExpired()).isTrue();

    }

    @Test
    public void test_getById(){
        when(userRepo.findById(any())).thenReturn(Optional.of(user));
        User expectedUser = userService.getById(user.getId());
        assertThat(expectedUser).isNotNull();
        assertThat(expectedUser.getGender()).isEqualTo(Gender.Male);
        assertThat(expectedUser.getId()).isEqualTo(user.getId());
        assertThat(expectedUser.getFirstName()).isEqualTo("Raj");
        assertThat(expectedUser.getProvider()).isEqualTo(AuthProvider.google);
        assertThat(expectedUser.getRole().getId()).isEqualTo(role.getId());

    }

    @Test
    public void test_existsByEmail(){
        String email = "abc@gmail.com";
        when(userRepo.existsByEmail(anyString())).thenReturn(true);
        Boolean expectedResult = userService.existsByEmail(email);
        assertThat(expectedResult).isTrue();
    }

    @Test
    public void test_signUp(){
        when(roleRepo.findByNameIgnoreCase(anyString())).thenReturn(role);
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("12345");
        when(userRepo.save(any(User.class))).thenReturn(user);
        User expectedUser = userService.signUp(signup);
        assertThat(expectedUser).isNotNull();
        assertThat(expectedUser.getRoleId()).isEqualTo(role.getId());
        assertThat(expectedUser.getPassword()).isEqualTo("12345");
        assertThat(expectedUser.getLastName()).isNotEmpty();

    }

    @Test
    public void test_getUserDetails(){
        when(userRepo.findById(any())).thenReturn(Optional.of(user));
        UserResponseDto expectedUser = userService.getUserDetails(user.getId());
        assertThat(expectedUser).isNotNull();
        assertThat(expectedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(expectedUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(expectedUser.getMobileNumber()).isEqualTo(user.getMobileNumber());
        assertThat(expectedUser.getGender()).isEqualTo(user.getGender());
    }

    @Test
    public void test_updateUser(){

        when(userRepo.findById(any())).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenReturn(user);
        UserResponseDto expectedUser = userService.updateUser(updateUserDto, user.getId());
        assertThat(expectedUser).isNotNull();
        assertThat(expectedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(expectedUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(expectedUser.getMobileNumber()).isEqualTo(user.getMobileNumber());
        assertThat(expectedUser.getGender()).isEqualTo(user.getGender());
    }

    @Test
    public void test_updateStatus(){
        when(userRepo.findById(any())).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenReturn(user);
        userService.updateStatus(user.getId(), ONLINE.toString());
        verify(userRepo, times(1)).save(any(User.class));
        verify(userRepo, times(1)).findById(any(UUID.class));

    }

    @Test
    public void test_findByNameContainingIgnoreCase() {

        User secondUser = User.builder().id(UUID.randomUUID()).firstName("RAM").middleName("kumar")
                .lastName("SINGH").mobileNumber("82938324894").password("12345").provider(AuthProvider.facebook)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("facebook123")
                .emailVerified(false).gender(Gender.Male).build();

        List<User> usersList = List.of(user, secondUser);
        when(userRepo.findAll()).thenReturn(usersList);
        List<UserResponseDto> userResponseDtoList = userService.findByNameContainingIgnoreCase("");
        assertThat(userResponseDtoList.size()).isNotNull();
        assertThat(2).isEqualTo(userResponseDtoList.size());
        assertThat(secondUser.getLastName()).isEqualTo(userResponseDtoList.get(1).getLastName());
        assertThat(user.getFirstName()).isEqualTo(userResponseDtoList.get(0).getFirstName());

        when(userRepo.findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndMiddleNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase(
                anyInt(), anyString(), anyInt(), anyString(), anyInt(), anyString())).thenReturn(List.of(user));
        List<UserResponseDto> userResponseDto = userService.findByNameContainingIgnoreCase("Raj");
        assertThat(1).isEqualTo(userResponseDto.size());
        assertThat(user.getId()).isEqualTo(userResponseDto.get(0).getId());
        assertThat(Gender.Male).isEqualTo(userResponseDto.get(0).getGender());

    }

    @Test
    public void test_findByNameAndStatus(){
        Pageable pageable = Mockito.mock(Pageable.class);

        User secondUser = User.builder().id(UUID.randomUUID()).firstName("RAM").middleName("kumar")
                .lastName("SINGH").mobileNumber("82938324894").password("12345").provider(AuthProvider.facebook)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("facebook123")
                .emailVerified(false).gender(Gender.Male).build();
        Page<User> page = new PageImpl<>(List.of(user, secondUser), pageable, 2);

        //null case
        when(userRepo.findAll(pageable)).thenReturn(page);
        Pagination pagination = userService.findByNameAndStatus(null, null, pageable);
        assertThat(pagination).isNotNull();
        assertThat(pagination.getCount()).isEqualTo(2);

        // status case
        when(userRepo.findByStatus(anyInt(), any())).thenReturn(page);
        Pagination pagination1 = userService.findByNameAndStatus(1, null, pageable);
        assertThat(pagination1).isNotNull();
        assertThat(pagination1.getData().size()).isEqualTo(2);

        //both case
        when(userRepo.findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndMiddleNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase(
                1, "Raj", 1, "Raj", 1, "Raj", pageable
        )).thenReturn(page);
        Pagination pagination2 = userService.findByNameAndStatus(1, "Raj", pageable);
        Pagination pagination3 = userService.findByNameAndStatus(null, "Raj", pageable);

        assertThat(pagination2.getData()).isNotNull();
        assertThat(pagination2.getCount()).isEqualTo(2);
        assertThat(pagination3.getData().size()).isEqualTo(2);

    }

    @Test
    public void test_findByNameOrEmailContainingIgnoreCase_nullCase(){
        User secondUser = User.builder().id(UUID.randomUUID()).firstName("RAM").middleName("kumar")
                .lastName("SINGH").mobileNumber("82938324894").password("12345").provider(AuthProvider.facebook)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("facebook123")
                .emailVerified(false).gender(Gender.Male).build();

        //null case
        when(userRepo.findAll()).thenReturn(List.of(user, secondUser));
        when(conversationParticipantRepo.findBySingleConversationIdExist(any(), any())).thenReturn(null);
        List<ChatUserDto> chatUserDtoList = userService.findByNameOrEmailContainingIgnoreCase(user.getId(), null);

        Assertions.assertThat(chatUserDtoList).isNotNull();
        Assertions.assertThat(2).isEqualTo(chatUserDtoList.size());
        Assertions.assertThat(user.getId()).isEqualTo(chatUserDtoList.get(0).getId());


    }


    @Test
    public void test_findByNameOrEmailContainingIgnoreCase_validCase() {

        User secondUser = User.builder().id(UUID.randomUUID()).firstName("RAM").middleName("kumar")
                .lastName("SINGH").mobileNumber("82938324894").password("12345").provider(AuthProvider.facebook)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("facebook123")
                .emailVerified(false).gender(Gender.Male).build();

        UUID userId = UUID.randomUUID();
        UUID conversationId = UUID.randomUUID();

        ConversationReadReceipt conversationReadReceipt = ConversationReadReceipt.builder()
                .id(UUID.randomUUID())
                .messageId(UUID.randomUUID())
                .receiverParticipantId(UUID.randomUUID())
                .messageStatus(MessageStatus.Send)
                .build();

        ConversationMessage conversationMessage = ConversationMessage.builder()
                .id(UUID.randomUUID())
                .conversationId(conversationId)
                .senderParticipantId(UUID.randomUUID())
                .body("Hi, How are you?")
                .media(List.of("image.png"))
                .conversationReadReceipts(List.of(conversationReadReceipt))
                .build();

        ConversationParticipant conversationParticipant = ConversationParticipant.builder()
                .id(UUID.randomUUID())
                .conversationId(conversationId)
                .userId(userId)
                .conversationMessages(List.of(conversationMessage))
                .conversationReadReceipts(List.of(conversationReadReceipt))
                .build();

        when(userRepo.findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndMiddleNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase(
                anyInt(), anyString(), anyInt(), anyString(), anyInt(), anyString()
        )).thenReturn(List.of(user, secondUser));
        when(conversationParticipantRepo.findBySingleConversationIdExist(any(UUID.class), any(UUID.class))).thenReturn(UUID.randomUUID());
        when(conversationParticipantRepo.findByConversationIdAndUserId(any(UUID.class), any(UUID.class))).thenReturn(conversationParticipant);

        List<ChatUserDto> newChatDto = userService.findByNameOrEmailContainingIgnoreCase(user.getId(), "ram");
        Assertions.assertThat(newChatDto).isNotNull();
        Assertions.assertThat(newChatDto.size()).isEqualTo(2);

    }


}
