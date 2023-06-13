package com.technoidentity.security.oauth2.user;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class FacebookOAuth2UserInfoTests {

   // @Mock OAuth2UserInfo oAuth2UserInfo;
    private Map<String, Object> attributes;
    private Map<String, Object> pictureObj;
    Map<String, Object>  dataObj;

    @BeforeEach
    public void init(){
        attributes = new HashMap<>();
        pictureObj = new HashMap<>();
        dataObj = new HashMap<>();
        attributes.put("id", "12345");
        attributes.put("name", "Ram");
        attributes.put("email", "abcc@gmail.com");
        attributes.put("picture", pictureObj);
        pictureObj.put("data", dataObj);
        dataObj.put("url", "http://localhost:8080/picture.jpg");
    }

    @Test
    public void test_Getters(){
        FacebookOAuth2UserInfo facebookOAuth2UserInfo = new FacebookOAuth2UserInfo(attributes);
        Assertions.assertThat(facebookOAuth2UserInfo.getId()).isEqualTo("12345");
        Assertions.assertThat(facebookOAuth2UserInfo.getName()).isEqualTo("Ram");
        Assertions.assertThat(facebookOAuth2UserInfo.getEmail()).isEqualTo("abcc@gmail.com");
        Assertions.assertThat(facebookOAuth2UserInfo.getImageUrl()).isEqualTo("http://localhost:8080/picture.jpg");
        Assertions.assertThat(facebookOAuth2UserInfo.getFirstName()).isEqualTo(null);
        Assertions.assertThat(facebookOAuth2UserInfo.getLastName()).isEqualTo(null);

    }

    @Test
    public void test_getImageUrl_null(){
        attributes.remove("picture");
        FacebookOAuth2UserInfo facebookOAuth2UserInfo = new FacebookOAuth2UserInfo(attributes);
        Assertions.assertThat(facebookOAuth2UserInfo.getImageUrl()).isEqualTo(null);
    }

}
