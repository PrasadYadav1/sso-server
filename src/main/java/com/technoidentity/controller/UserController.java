package com.technoidentity.controller;

import com.technoidentity.dto.*;
import com.technoidentity.entity.User;
import com.technoidentity.security.CurrentUser;
import com.technoidentity.service.FileStorageService;
import com.technoidentity.service.ImageStorageService;
import com.technoidentity.service.UserPrincipal;
import com.technoidentity.service.UserService;
import com.technoidentity.util.Pagination;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
@Api(tags = "Users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ImageStorageService imageStorageService;


    @GetMapping("/me")
    public UserDto getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {

        User userDetails = userService.getById(userPrincipal.getId());
        UserDto userDto = new UserDto(userDetails.getId(), userDetails.getRole().getId(), userDetails.getRole().getName(),
                userDetails.getFirstName(), userDetails.getMiddleName(), userDetails.getLastName(), userDetails.getEmail(), userDetails.getProfileImage(), userDetails.getGender(), userDetails.getMobileNumber(),userDetails.getDateOfBirth());
        return userDto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable("id") UUID id) {
        UserResponseDto userResponseDto = userService.getUserDetails(id);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);

    }

    @GetMapping("/all-users")
    public ResponseEntity<? >  findByName(@RequestParam(required = false) String name) {
        List<UserResponseDto> users = userService.findByNameContainingIgnoreCase(name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping("/chat-users")
    public ResponseEntity<? >  findAllChatUsers(@RequestParam(required = false) String name) {
        UserPrincipal userDetails =
                (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ChatUserDto> users = userService.findByNameOrEmailContainingIgnoreCase(userDetails.getId(),name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PutMapping("/user-status/{id}")
    public ResponseEntity<?> updateUserStatus(@PathVariable("id") UUID id, @RequestParam("status") String status) {
        userService.updateStatus(id, status);
        return new ResponseEntity<>(new ApiResponse(true, "User status updated successfully"), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto, @PathVariable("id") UUID id){
        UserResponseDto userDetails = userService.updateUser(updateUserDto, id);
        return new ResponseEntity<>(new UpdateUserDetailsApiResponse(userDetails, "User updated successfully"), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Pagination> users(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                            @RequestParam(required = false) String search,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                            @RequestParam(value = "sortOrder", defaultValue = "DESC") String sortOrder){
        Sort sortOrderData = sortOrder.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sortOrderData);
        Pagination data = userService.findByNameAndStatus(status, search, pageable);
        return new ResponseEntity<>(data, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = "/profile-image/{id}" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> profileImage(@PathVariable("id") UUID id, @RequestPart("image") MultipartFile file){
        //String BaseURL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        //System.out.println(BaseURL);
        String imagePath = imageStorageService.save(id, file);
        return new ResponseEntity<>(new ImageUploadResponse(imagePath, "Image uploaded successfully"), HttpStatus.CREATED);

    }



    @GetMapping(value = "profile-image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> downloadImage(@PathVariable("id") UUID id ){
        Resource downloadedImage = imageStorageService.downloadImage(id);
        //HttpHeaders headers = new HttpHeaders();
        //headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadedImage.getFilename() + "\"");
        return ResponseEntity.ok()
                //.headers(headers)
                .body(downloadedImage);

    }

    @DeleteMapping("/profile-image/{id}")
    public ResponseEntity<? > deleteImage(@PathVariable("id") UUID id){
        imageStorageService.delete(id);
        return new ResponseEntity<>(new ApiResponse(true, "Image deleted successfully"), HttpStatus.OK);
    }

}
