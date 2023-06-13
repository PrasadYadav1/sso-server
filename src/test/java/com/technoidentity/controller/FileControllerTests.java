package com.technoidentity.controller;

import com.technoidentity.security.CustomUserDetailsService;
import com.technoidentity.security.TokenProvider;
import com.technoidentity.security.oauth2.CustomOAuth2UserService;
import com.technoidentity.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.technoidentity.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.technoidentity.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(FileController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FileControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;
    @MockBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    @MockBean
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private FileStorageService fileStorageService;

    @Test
    public void test_fileUpload() throws Exception {

        MockMultipartFile multipartFile = new MockMultipartFile("file", "file.txt", MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes());
        when(fileStorageService.save(any())).thenReturn("file-download/file.txt");

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/file-upload").file(multipartFile));
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("File uploaded successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uri").value("http://hctra-dashboard.devti.in:9090/file-download/file.txt"));
    }

    @Test
    public void test_fileDownload() throws Exception {
        //Resource resource = Mockito.mock(Resource.class);
        Resource resource = new ByteArrayResource("Test File content".getBytes());
        when(fileStorageService.download(anyString())).thenReturn(resource);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/files/file-download")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("fileName", "file.txt"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.CONTENT_DISPOSITION));
    }

    @Test
    public void test_fileDelete() throws Exception {
        doNothing().when(fileStorageService).delete(anyString());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/files/file-delete")
                .param("fileName", "file.txt"));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("File deleted successfully"));
    }




}
