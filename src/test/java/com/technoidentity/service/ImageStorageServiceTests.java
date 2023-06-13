/*
package com.technoidentity.service;

import com.technoidentity.entity.Role;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import com.technoidentity.exception.BadRequestException;
import com.technoidentity.repository.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageStorageServiceTests {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private ImageStorageServiceImpl imageStorageService;
    private Role role;
    private User user;

    @BeforeEach
    public void init(){
        role = Role.builder().id(UUID.randomUUID()).name("Raj").description("write-role").build();
        user = User.builder().id(UUID.randomUUID()).firstName("Raj").middleName("kumar")
                .lastName("Vera").mobileNumber("8293894").password("12345").provider(AuthProvider.google)
                .role(role).email("abc@gmail.com").roleId(role.getId()).providerId("google123")
                .emailVerified(false).gender(Gender.Male).build();
    }

    @Test
    public void test_save(){
        MultipartFile jpeg_file = new MockMultipartFile("file", "unit-test-case.jpeg", MediaType.IMAGE_JPEG_VALUE, "Test content".getBytes());

        when(userRepo.findById(any(UUID.class))).thenReturn(Optional.of(user));
        String expectedURL = imageStorageService.save(user.getId(), jpeg_file);

        Assertions.assertThat(expectedURL).isNotNull();
        Assertions.assertThat(expectedURL).contains("api/users/profile-image/");

        //assertions case
        MultipartFile invalid_file = new MockMultipartFile("unit-test", "unit-test-case.txt", MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());
        assertThrows(BadRequestException.class, () -> imageStorageService.save(user.getId(), invalid_file));
    }

    @Test
    public void test_download() {
        //Exception case
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        //Resource resource = imageStorageService.downloadImage(user.getId());
        assertThrows(BadRequestException.class, () -> imageStorageService.downloadImage(user.getId()));
    }

    @Test
    public void test_delete(){
        //Exception case
        when(userRepo.findById(any(UUID.class))).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> imageStorageService.delete(user.getId()));
    }

}
*/