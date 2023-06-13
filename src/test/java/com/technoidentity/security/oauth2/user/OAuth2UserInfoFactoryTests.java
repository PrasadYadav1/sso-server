package com.technoidentity.security.oauth2.user;

import com.technoidentity.exception.OAuth2AuthenticationProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class OAuth2UserInfoFactoryTests {


    OAuth2UserInfo oAuth2UserInfo;

    @Test
    public void test_getOAuth2UserInfo(){

        oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo("google", null);
        Assertions.assertThat(oAuth2UserInfo).isInstanceOf(GoogleOAuth2UserInfo.class);

        oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo("facebook", null);
        Assertions.assertThat(oAuth2UserInfo).isInstanceOf(FacebookOAuth2UserInfo.class);

        oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo("github", null);
        Assertions.assertThat(oAuth2UserInfo).isInstanceOf(GithubOAuth2UserInfo.class);

        assertThrows(OAuth2AuthenticationProcessingException.class, () -> {
            OAuth2UserInfoFactory.getOAuth2UserInfo("unknown", null);
        });

    }
}
