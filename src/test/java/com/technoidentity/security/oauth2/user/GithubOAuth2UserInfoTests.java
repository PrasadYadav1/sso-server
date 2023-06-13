package com.technoidentity.security.oauth2.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class GithubOAuth2UserInfoTests {

    private Map<String, Object> attributes;

    @BeforeEach
    public void init(){
        attributes = new HashMap<>();
        attributes.put("id", 12);
        attributes.put("name", "Ram");
        attributes.put("email", "abcc@gmail.com");
        attributes.put("avatar_url", "http://localhost:8080/picture.jpg");
    }

    @Test
    public void test_Getters(){
        GithubOAuth2UserInfo githubOAuth2UserInfo = new GithubOAuth2UserInfo(attributes);
        Assertions.assertThat(githubOAuth2UserInfo.getId()).isEqualTo(String.valueOf(12));
        Assertions.assertThat(githubOAuth2UserInfo.getName()).isEqualTo("Ram");
        Assertions.assertThat(githubOAuth2UserInfo.getEmail()).isEqualTo("abcc@gmail.com");
        Assertions.assertThat(githubOAuth2UserInfo.getImageUrl()).isEqualTo("http://localhost:8080/picture.jpg");
        Assertions.assertThat(githubOAuth2UserInfo.getFirstName()).isEqualTo("Ram");
        Assertions.assertThat(githubOAuth2UserInfo.getLastName()).isEqualTo("Ram");
    }

}
