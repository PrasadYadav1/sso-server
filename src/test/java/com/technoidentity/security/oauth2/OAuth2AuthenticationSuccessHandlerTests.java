package com.technoidentity.security.oauth2;

import com.technoidentity.config.AppProperties;
import com.technoidentity.exception.MissingHeaderInfoException;
import com.technoidentity.security.TokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.technoidentity.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OAuth2AuthenticationSuccessHandlerTests {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AppProperties appProperties;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    @InjectMocks
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private String targetUrl;
    private Authentication authentication;

    @BeforeEach
    public void init(){
        authentication = new UsernamePasswordAuthenticationToken("username", "password");
        targetUrl = "/home";
    }

    @Test
    public void test_onAuthenticationSuccess_throwsException() throws Exception {
        String redirectUri = "http://localhost:3000/oauth2/redirect";
        Cookie cookie = new Cookie(REDIRECT_URI_PARAM_COOKIE_NAME, redirectUri);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        oAuth2AuthenticationSuccessHandler.setDefaultTargetUrl(targetUrl);
        when(appProperties.getOauth2()).thenReturn(new AppProperties.OAuth2());
        assertThrows(MissingHeaderInfoException.class, () -> {
            oAuth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        });
    }

    @Test
    public void test_onAuthenticationSuccess_withoutException() throws ServletException, IOException {
        String redirectUri = "http://localhost:3000/oauth2/redirect";
        Cookie cookie = new Cookie(REDIRECT_URI_PARAM_COOKIE_NAME, redirectUri);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        oAuth2AuthenticationSuccessHandler.setDefaultTargetUrl(targetUrl);
        when(tokenProvider.createToken(authentication)).thenReturn("token");
        when(appProperties.getOauth2()).thenReturn(new AppProperties.OAuth2().authorizedRedirectUris(List.of(redirectUri)));
        when(response.isCommitted()).thenReturn(false);
        oAuth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        verify(httpCookieOAuth2AuthorizationRequestRepository, Mockito.times(1)).removeAuthorizationRequestCookies(request, response);

    }

    @Test
    public void test_determineTargetUrl() throws ServletException, IOException {
        String redirectUri = "http://localhost:3000/oauth2/redirect";
        Cookie cookie = new Cookie(REDIRECT_URI_PARAM_COOKIE_NAME, redirectUri);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        oAuth2AuthenticationSuccessHandler.setDefaultTargetUrl(targetUrl);
        when(tokenProvider.createToken(authentication)).thenReturn("token");
        when(appProperties.getOauth2()).thenReturn(new AppProperties.OAuth2().authorizedRedirectUris(List.of(redirectUri)));
        String url = oAuth2AuthenticationSuccessHandler.determineTargetUrl(request, response, authentication);
        Assertions.assertThat(url).isEqualTo(redirectUri + "?token=token");

    }
}
