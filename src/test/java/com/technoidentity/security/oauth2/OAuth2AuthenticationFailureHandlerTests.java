package com.technoidentity.security.oauth2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.technoidentity.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OAuth2AuthenticationFailureHandlerTests {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AuthenticationException exception;
    @Mock
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    @Mock
    private RedirectStrategy redirectStrategy;
    @InjectMocks
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Test
    public void test_onAuthenticationFailure() throws Exception {
        String redirectUri = "http://localhost:8080/oauth2/redirect";
        String errorMessage = "Invalid Credentials";
        Cookie cookie = new Cookie(REDIRECT_URI_PARAM_COOKIE_NAME, redirectUri);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        when(exception.getLocalizedMessage()).thenReturn(errorMessage);
        oAuth2AuthenticationFailureHandler.setRedirectStrategy(redirectStrategy);
        oAuth2AuthenticationFailureHandler.onAuthenticationFailure(request, response, exception);

        verify(httpCookieOAuth2AuthorizationRequestRepository).removeAuthorizationRequestCookies(request, response);
        verify(redirectStrategy).sendRedirect(request, response, redirectUri + "?error=" + errorMessage);
    }
}
