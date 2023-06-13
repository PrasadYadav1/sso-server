/*
package com.technoidentity.service;

import com.technoidentity.exception.BadRequestException;
import com.technoidentity.repository.ConversationMessageRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTests {

    @Mock
    private ConversationMessageRepo conversationMessageRepo;

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    @BeforeEach
    public void init(){

    }

    @Test
    public void test_save(){
        MultipartFile file = new MockMultipartFile("unit-test", "unit-test-case.txt", MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());
        String expectedURL = fileStorageService.save(file);

        Assertions.assertThat(expectedURL).isNotEmpty();
        Assertions.assertThat(expectedURL).contains("unit-test-case.txt");
        Assertions.assertThat(expectedURL).contains("api/files/file-download");

        //exception case
        assertThrows(BadRequestException.class, ()-> fileStorageService.save(null));

    }

    @Test
    public void test_download() throws IOException {
        fileStorageService.init();
        String fileName = "unit-test-case.txt";
        assertThrows(BadRequestException.class, () -> fileStorageService.download(fileName));
    }

    @Test
    public void test_delete() {

        String fileName = "unit-test-case.txt";
        assertThrows(BadRequestException.class, () -> fileStorageService.delete(fileName));

    }

}
*/
