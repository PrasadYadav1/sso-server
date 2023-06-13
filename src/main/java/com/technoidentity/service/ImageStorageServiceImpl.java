package com.technoidentity.service;

import com.technoidentity.entity.User;
import com.technoidentity.exception.BadRequestException;
import com.technoidentity.exception.ResourceNotFoundException;
import com.technoidentity.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {
    private final long maxAllowedSize = 5 * 1024 * 1024;
    private final Path ROOT = Paths.get(System.getProperty("user.dir"), "spring-boot", "sso-oauth-two-authentication-api", "src", "main", "resources", "image","users");

    @Autowired
    private UserRepo userRepo;

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(ROOT);
    }

    @Override
    @Transactional
    public String save(UUID id, MultipartFile file) {
        User user = userRepo.findById(id).orElse(null);

        if(user == null )  throw new ResourceNotFoundException("user", "id : ", id);
        if(file == null || file.isEmpty())  throw new BadRequestException("Request must contain a file");
        long fileSize = file.getSize();
        if(fileSize > maxAllowedSize) throw new BadRequestException("Size of image should be less than 5MB");
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if(!isSupportedFormat(fileExtension))  throw new BadRequestException("Only jpg, jpeg, png files are supported");

        String fileName = user.getId().toString()+"-"+file.getOriginalFilename();
        Path imagePath = Paths.get(ROOT + File.separator+ fileName);
        //TODO : Need to make it dynamic.
        String finalURL = "http://hctra-dashboard.devti.in:9090/" + "api/users/profile-image/"+id;

        try {
            // delete already existed profile Image
            Path path = findImage(id);
            if(path != null)  Files.delete(path);

            Files.copy(file.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfileImage(finalURL);
            user.setUpdatedAt(new Date());
            userRepo.save(user);
        }
        catch (Exception ex){
            throw  new RuntimeException("Error in storing file. Error : " + ex.getMessage());
        }
        return finalURL;
    }

    private boolean isSupportedFormat(String fileExtension) {
        String[] supportedExtension = {"jpg", "jpeg", "png"};
        for(String extension : supportedExtension){
            if(fileExtension.equalsIgnoreCase(extension))
                return true;
        }
        return false;
    }

    @Override
    public Resource downloadImage(UUID id) {
        User user = userRepo.findById(id).orElse(null);
        if(user == null )  throw new ResourceNotFoundException("user", "id : ", id);

        try {
            Path filePath = findImage(id);
            if(filePath == null) throw new BadRequestException("Profile image doesn't exist for user : "+id);
            Resource resource =  new UrlResource(filePath.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }
            else{
                throw new RuntimeException("Couldn't read the file.");
            }
        } catch (IOException ex){
            throw new RuntimeException("Error : " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        User user = userRepo.findById(id).orElse(null);
        if(user == null )  throw new ResourceNotFoundException("user", "id : ", id);

        try{
            Path filePath = findImage(id);
            if(filePath == null) throw new BadRequestException("Profile image doesn't exist for user : "+id);
            Files.delete(filePath);
            user.setProfileImage("");
        }
        catch (IOException ex){
            throw new RuntimeException("Couldn't delete file. Error : " + ex.getMessage());
        }
    }

    private Path findImage(UUID id){
        try {
            Optional<Path> filePath = Files.walk(ROOT)
                    .filter(path -> path.getFileName().toString().contains(id.toString()))
                    .findAny();
            return filePath.orElse(null);
        }
        catch (IOException ex){
            throw new RuntimeException("File not found. Error : " + ex.getMessage());
        }
    }

}
