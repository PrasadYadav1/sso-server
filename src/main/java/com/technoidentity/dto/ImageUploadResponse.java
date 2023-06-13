package com.technoidentity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ImageUploadResponse {
    private String Uri;

    private String message;
}
