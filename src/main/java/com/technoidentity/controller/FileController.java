
package com.technoidentity.controller;

import com.technoidentity.dto.ApiResponse;
import com.technoidentity.dto.ImageUploadResponse;
import com.technoidentity.repository.ConversationMessageRepo;
import com.technoidentity.service.FileStorageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/files")
@Api(tags = "Files")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;


    @GetMapping(value = "/file-download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> fileDownload(@RequestParam("fileName") String fileName){
        Resource resource = fileStorageService.download(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @PostMapping(value = "/file-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> fileUpload(@RequestPart("file") MultipartFile file){
        String path = fileStorageService.save(file);
        //String BaseURL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String BaseURL = "http://hctra-dashboard.devti.in:9090/";
        String finalPath = BaseURL + path;
        return new ResponseEntity<>(new ImageUploadResponse(finalPath, "File uploaded successfully"), HttpStatus.CREATED);
    }

    @DeleteMapping("/file-delete")
    public ResponseEntity<?> fileDelete(@RequestParam("fileName") String fileName){
        fileStorageService.delete(fileName);
        return new ResponseEntity<>(new ApiResponse(true, "File deleted successfully"), HttpStatus.OK);
    }





}

