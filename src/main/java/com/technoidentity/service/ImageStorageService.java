package com.technoidentity.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageStorageService {
    String save(UUID id, MultipartFile file);
    void delete(UUID id);

    Resource downloadImage(UUID id);
}
