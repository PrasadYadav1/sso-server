package com.technoidentity.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileStorageService {

    String save( MultipartFile file);
    Resource download(String fileName);
    void delete(String fileName);
}
