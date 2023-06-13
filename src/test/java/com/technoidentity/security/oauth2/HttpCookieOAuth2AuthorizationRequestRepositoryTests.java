package com.technoidentity.security.oauth2;

import com.technoidentity.util.CookieUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.technoidentity.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)
public class HttpCookieOAuth2AuthorizationRequestRepositoryTests {

    @Mock
    HttpServletRequest httpRequest;
    @Mock
    HttpServletResponse httpResponse;
    @InjectMocks
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private Cookie cookie;
    Map<String, Object> parameters;
    OAuth2AuthorizationRequest authorizationRequest;

    @BeforeEach
    public void init(){
        //cookie = new Cookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, "serialized-oauth2-auth-request");
        parameters = new LinkedHashMap<>();
        parameters.put("client_id", "test-client-id");
        parameters.put("redirect_uri", "http://localhost:8080/callback");
        parameters.put("response_type", "code");
        parameters.put("scope", "openid profile email");

        authorizationRequest =  OAuth2AuthorizationRequest.authorizationCode()
                .clientId("test-client-id")
                .redirectUri("http://localhost:8080/callback")
                .scopes(Set.of("openid", "profile", "username"))
                .state("test-state")
                .authorizationUri("http://localhost:8080/oauth2/authorize")
                .additionalParameters(parameters)
                .build();
        cookie = new Cookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest));
    }

    @Test
    public void test_loadAuthorizationRequest(){
        when(httpRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        OAuth2AuthorizationRequest authorizationRequest = httpCookieOAuth2AuthorizationRequestRepository.loadAuthorizationRequest(httpRequest);
        Assertions.assertThat(authorizationRequest).isNotNull();
        Assertions.assertThat(authorizationRequest.getAdditionalParameters()).isEqualTo(parameters);
        Assertions.assertThat(authorizationRequest.getAuthorizationUri()).isEqualTo("http://localhost:8080/oauth2/authorize");
        Assertions.assertThat(authorizationRequest.getClientId()).isEqualTo("test-client-id");
        Assertions.assertThat(authorizationRequest.getRedirectUri()).isEqualTo("http://localhost:8080/callback");
        Assertions.assertThat(authorizationRequest.getScopes()).isEqualTo(Set.of("openid", "profile", "username"));
        Assertions.assertThat(authorizationRequest.getState()).isEqualTo("test-state");
    }

    @Test
    public void test_removeAuthorizationRequest(){
        when(httpRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        OAuth2AuthorizationRequest authorizationRequest = httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequest(httpRequest);
        Assertions.assertThat(authorizationRequest).isNotNull();
        Assertions.assertThat(authorizationRequest.getAdditionalParameters()).isEqualTo(parameters);
        Assertions.assertThat(authorizationRequest.getAuthorizationUri()).isEqualTo("http://localhost:8080/oauth2/authorize");

    }

    @Test
    public void test_saveAuthorizationRequest(){

        httpCookieOAuth2AuthorizationRequestRepository.saveAuthorizationRequest(authorizationRequest, httpRequest, httpResponse);
        verify(httpResponse, times(1)).addCookie(any(Cookie.class));

        //null case
        httpCookieOAuth2AuthorizationRequestRepository.saveAuthorizationRequest(null, httpRequest, httpResponse);
        verify(httpResponse, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    public void test_removeAuthorizationRequestCookies(){
        when(httpRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(httpRequest, httpResponse);

        verify(httpResponse, times(1)).addCookie(any(Cookie.class));


    }
}
