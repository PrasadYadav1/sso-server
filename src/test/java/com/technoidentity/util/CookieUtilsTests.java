package com.technoidentity.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CookieUtilsTests {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private Cookie cookie1;
    private Cookie cookie2;

    @BeforeEach
    public void init(){
        cookie1 = new Cookie("cookie1", "value1");
        cookie2 = new Cookie("cookie2", "value2");
    }

    @Test
    public void test_getCookie(){
        Cookie[] cookies = new Cookie[]{cookie1, cookie2};
        when(request.getCookies()).thenReturn(cookies);
        Optional<Cookie> cookie = CookieUtils.getCookie(request, "cookie1");
        assertThat(cookie.get()).isEqualTo(cookie1);

        //empty Case
        when(request.getCookies()).thenReturn(new Cookie[0]);
        Optional<Cookie> result = CookieUtils.getCookie(request, "cookie1");
        assertThat(result).isEmpty();

    }
    @Test
    public void test_addCookie(){

        CookieUtils.addCookie(response, "cookie1", "value1", 500);
        Mockito.verify(response).addCookie(argThat(
                cookie -> {
                    return cookie.getName().equals("cookie1")
                            && cookie.getValue().equals("value1")
                            && cookie.isHttpOnly()
                            && cookie.getPath().equals("/")
                            && cookie.getMaxAge() == 500;
                }
        ));
    }
    @Test
    public void test_deleteCookie(){
        when(request.getCookies()).thenReturn(new Cookie[]{cookie1, cookie2});
        CookieUtils.deleteCookie(request, response, "cookie1");
        Mockito.verify(response).addCookie(argThat(
                cookie -> {
                    return cookie.getName().equals("cookie1")
                            && cookie.getValue().equals("")
                            && cookie.getPath().equals("/")
                            && cookie.getMaxAge() == 0;
                }
        ));
    }
    @Test
    public void test_serialize_deserialize() {
        String name = "Hello, world!";
        String serialized = CookieUtils.serialize(name);
        Cookie cookie = new Cookie("name", serialized);
        String deserialized = CookieUtils.deserialize(cookie, String.class);
        assertThat(name).isEqualTo(deserialized);
    }
    @Test
    public void test_deserialize() {
        Cookie cookie = new Cookie("name", "rO0ABXQADUhlbGxvLCB3b3JsZCE=");
        String deserialized = CookieUtils.deserialize(cookie, String.class);
        assertThat("Hello, world!").isEqualTo(deserialized);
    }

}
