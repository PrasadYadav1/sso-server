package com.technoidentity.service;

import com.technoidentity.dto.ChatUserDto;
import com.technoidentity.dto.SignUp;
import com.technoidentity.dto.UpdateUserDto;
import com.technoidentity.dto.UserResponseDto;
import com.technoidentity.entity.User;
import com.technoidentity.util.Pagination;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    UserDetails loadUserById(UUID id);

    User getById(UUID id);
    UserResponseDto getUserDetails(UUID id);

    Boolean existsByEmail(String email);
    User signUp(SignUp signUp);

    UserResponseDto updateUser(UpdateUserDto updateUserDto, UUID id);

    void updateStatus(UUID id, String status);

    List<UserResponseDto> findByNameContainingIgnoreCase(String name);


    List<ChatUserDto> findByNameOrEmailContainingIgnoreCase(UUID userId,String name);

    Pagination findByNameAndStatus(Integer status, String search, Pageable paging);

}
