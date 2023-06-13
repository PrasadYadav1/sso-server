package com.technoidentity.service;

import com.technoidentity.entity.Role;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserPrincipalTests {

    private Role role;
    private User user;
    private UserPrincipal userPrincipal;

    @BeforeEach
    public void init(){

        role = Role.builder().id(UUID.randomUUID()).name("Raj").description("write-role").build();
        user = User.builder().id(UUID.randomUUID()).firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345").provider(AuthProvider.google)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("google123")
                .emailVerified(false).gender(Gender.Male).build();
    }

    @Test
    public void test_create(){
        userPrincipal = UserPrincipal.create(user);
        Assertions.assertThat(userPrincipal).isNotNull();
        Assertions.assertThat(userPrincipal.getId()).isEqualTo(user.getId());
        Assertions.assertThat(userPrincipal.getFirstName()).isEqualTo(user.getFirstName());
        Assertions.assertThat(userPrincipal.getMiddleName()).isEqualTo(user.getMiddleName());
        Assertions.assertThat(userPrincipal.getLastName()).isEqualTo(user.getLastName());
        Assertions.assertThat(userPrincipal.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(userPrincipal.getPassword()).isEqualTo(user.getPassword());
        Assertions.assertThat(userPrincipal.getMobileNumber()).isEqualTo(user.getMobileNumber());
        Assertions.assertThat(userPrincipal.getName()).isEqualTo(String.valueOf(user.getId()));
    }

    @Test
    public void test_create_withAttributes(){
        Map< String, Object> attributes = new HashMap<>();
        attributes.put("id", user.getId());
        attributes.put("firstName", user.getFirstName());

        userPrincipal = UserPrincipal.create(user, attributes);
        Assertions.assertThat(userPrincipal).isNotNull();
        Assertions.assertThat(userPrincipal.getId()).isEqualTo(user.getId());
        Assertions.assertThat(userPrincipal.getAttributes()).isEqualTo(attributes);
        Assertions.assertThat(userPrincipal.getAttributes().get("id")).isEqualTo(attributes.get("id"));

    }


}
