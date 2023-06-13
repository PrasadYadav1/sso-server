package com.technoidentity.security;

import com.technoidentity.entity.Role;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import com.technoidentity.exception.ResourceNotFoundException;
import com.technoidentity.repository.UserRepo;
import com.technoidentity.service.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTests {
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    private Role role;
    private User user;
    private UserPrincipal userPrincipal;
    private UserDetails userDetails;

    @BeforeEach
    public void init(){
        role = Role.builder().id(UUID.randomUUID()).name("Raj").description("write-role").build();
        user = User.builder().id(UUID.randomUUID()).firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345").provider(AuthProvider.google)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("google123")
                .emailVerified(false).gender(Gender.Male).build();
        userDetails = UserPrincipal.create(user);
    }


    @Test
    public void test_loadUserByUsername() {
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        UserDetails expectedUserDetails = customUserDetailsService.loadUserByUsername("abc@gmail.com");
        assertThat(expectedUserDetails).isNotNull();
        assertThat(expectedUserDetails.getUsername()).isEqualTo(userDetails.getUsername());
        assertThat(expectedUserDetails.getAuthorities()).isEqualTo(userDetails.getAuthorities());
        assertThat(expectedUserDetails.getPassword()).isEqualTo("12345");
        assertThat(expectedUserDetails.isAccountNonExpired()).isTrue();

        //throws case
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(user.getEmail());
        });
    }

    @Test
    public void test_loadUserById(){

        when(userRepo.findById(any())).thenReturn(Optional.of(user));
        UserDetails expectedResult = customUserDetailsService.loadUserById(user.getId());
        assertThat(expectedResult).isNotNull();
        assertThat(expectedResult.getUsername()).isEqualTo(userDetails.getUsername());
        assertThat(expectedResult.getAuthorities()).isEqualTo(userDetails.getAuthorities());
        assertThat(expectedResult.isAccountNonLocked()).isTrue();
        assertThat(expectedResult.isEnabled()).isTrue();
        assertThat(expectedResult.isCredentialsNonExpired()).isTrue();

        //throws case
        when(userRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            customUserDetailsService.loadUserById(user.getId());
        });
    }

}
