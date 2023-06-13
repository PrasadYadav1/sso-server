
package com.technoidentity.service;

import com.amazonaws.services.apigateway.model.Op;
import com.technoidentity.entity.ConversationMessage;
import com.technoidentity.exception.BadRequestException;
import com.technoidentity.exception.ResourceNotFoundException;
import com.technoidentity.repository.ConversationMessageRepo;
import com.technoidentity.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService{

    @Autowired
    private ConversationMessageRepo conversationMessageRepo;
    private final Path ROOT = Paths.get(System.getProperty("user.dir"), "spring-boot", "sso-oauth-two-authentication-api", "src", "main", "resources", "image", "chat");

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(ROOT);
    }

    @Override
    public String save(MultipartFile file) {
        //TODO: We can do a regex check on fileName : 0-9, A-Z, a-z,-,_;
        if(file == null || file.isEmpty())  throw new BadRequestException("Request must contain a file");

        String fileName = UUID.randomUUID()+ "-" + file.getOriginalFilename();
        Path filePath = Path.of(ROOT + File.separator + fileName);
        String URL = "api/files/file-download"+ "?fileName="+fileName;

        try{
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error in storing file. Error : " + e.getMessage());
        }
        return URL;
    }

    @Override
    public Resource download( String fileName) {
        //TODO: We can do a regex check on fileName : 0-9, A-Z, a-z,-,_;
        if(fileName == null || fileName.isEmpty()) throw new BadRequestException("Request must contain a valid fileName.");
        try{
            Path filePath = findImage(fileName);
            if(filePath == null) throw new BadRequestException("File doesn't exist with name : " + fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists() || resource.isReadable())
                return  resource;
            else
                throw new RuntimeException("Couldn't read the file.");
        }
        catch (IOException ex){
            throw new RuntimeException("Couldn't read file. Error : " + ex.getMessage());
        }
    }

    @Override
    public void delete(String fileName) {
        //TODO: We can do a regex check on fileName : 0-9, A-Z, a-z,-,_;
        if(fileName == null || fileName.isEmpty()) throw new BadRequestException("Request must contain a valid fileName");
        try{
            Path filePath = findImage(fileName);
            if(filePath == null) throw new BadRequestException("File doesn't exist with name : " + fileName);
            Files.delete(filePath);
        }
        catch (IOException ex){
            throw new RuntimeException("Couldn't delete file. Error : " + ex.getMessage());
        }
    }


    private Path findImage(String fileName) {

        try {
            Optional<Path> filePath = Files.walk(ROOT)
                    .filter(path -> path.getFileName().toString().equals(fileName))
                    .findFirst();
            return filePath.orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("File not found. Error : " + e.getMessage());
        }

    }
}

