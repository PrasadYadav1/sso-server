package com.technoidentity.security;


import com.technoidentity.entity.Role;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import com.technoidentity.service.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenAuthenticationFilterTests {

    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private HttpServletRequest httpRequest;
    @Mock
    private HttpServletResponse httpResponse;
    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private TokenAuthenticationFilter tokenAuthenticationFilter;
    private String token;
    private Role role;
    private User user;
    private UserPrincipal userPrincipal;

    @BeforeEach
    public void init(){
        token = "Bearer sk-2BeH78p0ur0b36McNTpbT31lbkFJBPtklyC5w8hK710IUuXbZ";
        role = Role.builder().id(UUID.randomUUID()).name("Raj").description("write-role").build();
        user = User.builder().id(UUID.randomUUID()).firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345").provider(AuthProvider.google)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("google123")
                .emailVerified(false).gender(Gender.Male).build();
        userPrincipal = UserPrincipal.create(user);

    }

    @Test
    public void test_doFilterInternal() throws Exception {
        when(httpRequest.getHeader(anyString())).thenReturn(token);
        when(tokenProvider.validateToken(anyString())).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(anyString())).thenReturn(user.getId());
        when(customUserDetailsService.loadUserById(any(UUID.class))).thenReturn(userPrincipal);
        tokenAuthenticationFilter.doFilterInternal(httpRequest, httpResponse, filterChain);
        verify(customUserDetailsService, times(1)).loadUserById(user.getId());
        verify(filterChain, times(1)).doFilter(httpRequest, httpResponse);
        verify(httpRequest, times(1)).getHeader(anyString());
    }

    @Test
    public void test_doFilterInternal_nullCase() throws ServletException, IOException {
        when(httpRequest.getHeader(anyString())).thenReturn(null);
        tokenAuthenticationFilter.doFilterInternal(httpRequest, httpResponse, filterChain);
        verify(filterChain, times(1)).doFilter(httpRequest, httpResponse);
        verify(httpRequest, times(1)).getHeader(anyString());

    }
}
