package com.technoidentity.security.oauth2;

import com.technoidentity.security.RestAuthenticationEntryPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestAuthenticationEntryPointTests {

    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;
    @Mock
    private AuthenticationException exception;
    @InjectMocks
    private RestAuthenticationEntryPoint entryPoint;

    @Test
    public void test_commence() throws Exception {
        when(exception.getLocalizedMessage()).thenReturn("You are not authorized");
        entryPoint.commence(request, response, exception);
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized");

    }

}
