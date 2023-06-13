package com.technoidentity.security;

import com.technoidentity.config.AppProperties;
import com.technoidentity.entity.Role;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import com.technoidentity.service.UserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.event.LoggingEvent;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenProviderTests {

    @Mock
    private Authentication authentication;
    @Mock
    private AppProperties appProperties;
    @Mock
    private AppProperties.Auth auth;
    private TokenProvider tokenProvider;
    private String token;
    private UserPrincipal userPrincipal;
    private Role role;
    private User user;

    @BeforeEach
    public void init(){
        tokenProvider = new TokenProvider(appProperties);
        role = Role.builder().id(UUID.randomUUID()).name("Raj").description("write-role").build();
        user = User.builder().id(UUID.randomUUID()).firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345").provider(AuthProvider.google)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("google123")
                .emailVerified(false).gender(Gender.Male).build();
        userPrincipal = UserPrincipal.create(user);
    }


    @Test
    public void test_createToken(){
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(appProperties.getAuth()).thenReturn(auth);
        when(appProperties.getAuth().getTokenSecret()).thenReturn("some-secret");
        when(appProperties.getAuth().getTokenExpirationMSec()).thenReturn(864000000);
        token = tokenProvider.createToken(authentication);
        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(token.length()).isGreaterThan(0);
        Assertions.assertThat(token).contains(".");

    }

    @Test
    public void test_getUserIdFromToken() {
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(appProperties.getAuth()).thenReturn(auth);
        when(appProperties.getAuth().getTokenSecret()).thenReturn("some-secret");
        when(appProperties.getAuth().getTokenExpirationMSec()).thenReturn(864000000);
        token = tokenProvider.createToken(authentication);
        UUID expectedUserId = tokenProvider.getUserIdFromToken(token);
        Assertions.assertThat(expectedUserId).isNotNull();
        Assertions.assertThat(expectedUserId).isEqualTo(user.getId());

    }

    @Test
    public void test_validateToken(){

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(appProperties.getAuth()).thenReturn(auth);
        when(appProperties.getAuth().getTokenSecret()).thenReturn("some-secret");
        when(appProperties.getAuth().getTokenExpirationMSec()).thenReturn(864000000);
        token = tokenProvider.createToken(authentication);
        boolean expected = tokenProvider.validateToken(token);
        Assertions.assertThat(expected).isTrue();

        //Invalid JWT signature
        token = token + "invalid";
        expected = tokenProvider.validateToken(token);
        Assertions.assertThat(expected).isFalse();

        //Expired JWT Token
        when(appProperties.getAuth().getTokenExpirationMSec()).thenReturn(0);
        token = tokenProvider.createToken(authentication);
        expected = tokenProvider.validateToken(token);
        Assertions.assertThat(expected).isFalse();

        //Invalid JWT Token
        token = token.substring(3, token.length() - 1);
        expected = tokenProvider.validateToken(token);
        Assertions.assertThat(expected).isFalse();



    }

}
