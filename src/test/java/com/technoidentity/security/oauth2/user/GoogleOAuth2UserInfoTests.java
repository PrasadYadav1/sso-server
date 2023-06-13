package com.technoidentity.security.oauth2.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class GoogleOAuth2UserInfoTests {

    private Map<String, Object> attributes;

    @BeforeEach
    public void init(){
        attributes = new HashMap<>();
        attributes.put("sub", "5");
        attributes.put("name", "Ram");
        attributes.put("email", "abcc@gmail.com");
        attributes.put("given_name", "Ram_GIVEN");
        attributes.put("family_name", "Ram_FAMILY");
        attributes.put("picture", "http://localhost:8080/picture.jpg");
    }

    @Test
    public void test_Getters(){
        GoogleOAuth2UserInfo googleOAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);
        Assertions.assertThat(googleOAuth2UserInfo.getId()).isEqualTo("5");
        Assertions.assertThat(googleOAuth2UserInfo.getName()).isEqualTo("Ram");
        Assertions.assertThat(googleOAuth2UserInfo.getEmail()).isEqualTo("abcc@gmail.com");
        Assertions.assertThat(googleOAuth2UserInfo.getImageUrl()).isEqualTo("http://localhost:8080/picture.jpg");
        Assertions.assertThat(googleOAuth2UserInfo.getFirstName()).isEqualTo("Ram_GIVEN");
        Assertions.assertThat(googleOAuth2UserInfo.getLastName()).isEqualTo("Ram_FAMILY");
    }

}
