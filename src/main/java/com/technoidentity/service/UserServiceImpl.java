package com.technoidentity.service;

import com.technoidentity.dto.ChatUserDto;
import com.technoidentity.dto.SignUp;
import com.technoidentity.dto.UpdateUserDto;
import com.technoidentity.dto.UserResponseDto;
import com.technoidentity.entity.ConversationMessage;
import com.technoidentity.entity.ConversationParticipant;
import com.technoidentity.entity.Role;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.ConversationType;
import com.technoidentity.enums.MessageStatus;
import com.technoidentity.enums.UserStatus;
import com.technoidentity.exception.ResourceNotFoundException;
import com.technoidentity.repository.ConversationParticipantRepo;
import com.technoidentity.repository.RoleRepo;
import com.technoidentity.repository.UserRepo;
import com.technoidentity.util.Pagination;
import com.technoidentity.util.StringPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl implements UserService{

    @Autowired
    private  UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ConversationParticipantRepo conversationParticipantRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    StringPredicate isNullorEmpty = str -> str == null || str.trim().isEmpty();


    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
                );

        return UserPrincipal.create(user);
    }


    public UserDetails loadUserById(UUID id) {
        User user = userRepo.findById(id).get();

        if (user == null) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        return UserPrincipal.create(user);
    }

    @Override
    public User getById(UUID id) {
        User user = userRepo.findById(id).get();

        if (user == null) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        return user;
    }

    @Override
    public UserResponseDto getUserDetails(UUID id) {
        User userDetails = userRepo.findById(id).get();
        UserResponseDto userResponseDto = new UserResponseDto(userDetails.getId(), userDetails.getFirstName(), userDetails.getMiddleName(), userDetails.getLastName(),
                userDetails.getEmail(), userDetails.getProfileImage(), userDetails.getGender(), userDetails.getMobileNumber(),userDetails.getDateOfBirth(), userDetails.getUserStatus());
        if (userDetails == null) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        return userResponseDto;
    }


    @Override
    public Boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public User signUp(SignUp signUp) {
        Role  role = roleRepo.findByNameIgnoreCase("User");
        // Creating user's account
        User user = new User();
        user.setEmail(signUp.getEmail());
        user.setPassword(signUp.getPassword());
        user.setProvider(AuthProvider.local);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleId(role.getId());
        user.setFirstName(signUp.getFirstName());
        user.setMiddleName(signUp.getMiddleName());
        user.setLastName(signUp.getLastName());
        user.setGender(signUp.getGender());
        user.setEmailVerified(true);
        user.setDateOfBirth(signUp.getDateOfBirth());
        user.setMobileNumber(signUp.getMobileNumber());
        user.setProfileImage("");
        user.setCreatedBy(null);
        user.setCreatedAt(new Date());
        user.setUpdatedBy(null);
        user.setUpdatedAt(new Date());
        user.setStatus(1);

        User result = userRepo.save(user);
        return result;
    }

    @Override
    public UserResponseDto updateUser(UpdateUserDto updateUserDto, UUID id) {
        User user = userRepo.findById(id).get();
        if(user == null){
            throw new ResourceNotFoundException("User", "id", id);
        }

        if(updateUserDto.getFirstName() != null) user.setFirstName(updateUserDto.getFirstName());
        if(updateUserDto.getMiddleName() != null) user.setMiddleName(updateUserDto.getMiddleName());
        if(updateUserDto.getLastName() != null) user.setLastName(updateUserDto.getLastName());
        if(updateUserDto.getMobileNumber() != null) user.setMobileNumber(updateUserDto.getMobileNumber());
        if(updateUserDto.getGender() != null) user.setGender(updateUserDto.getGender());
        if(updateUserDto.getDateOfBirth() != null) user.setDateOfBirth(updateUserDto.getDateOfBirth());
        if(updateUserDto.getProfileImage() != null) user.setProfileImage(updateUserDto.getProfileImage());
        user.setUpdatedAt(new Date());

        User userDetails = userRepo.save(user);
        UserResponseDto userResponseDto = new UserResponseDto(userDetails.getId(), userDetails.getFirstName(), userDetails.getMiddleName(), userDetails.getLastName(),
                userDetails.getEmail(), userDetails.getProfileImage(), userDetails.getGender(), userDetails.getMobileNumber(),userDetails.getDateOfBirth(), userDetails.getUserStatus() );
        return userResponseDto;

    }

    @Override
    public void updateStatus(UUID id, String status) {
        User user = userRepo.findById(id).get();
        if(user == null){
            throw new ResourceNotFoundException("User", "id", id);
        }
        user.setUserStatus(UserStatus.valueOf(status));
        userRepo.save(user);
    }

    @Override
    public List<UserResponseDto> findByNameContainingIgnoreCase(String name) {
        List< UserResponseDto> users;
        List<User> userDetails;
        if(isNullorEmpty.apply(name))
            userDetails = userRepo.findAll();
        else
            userDetails = userRepo.findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndMiddleNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase( 1,name,1, name, 1,name);
        users = userDetails.stream()
                .map(user -> new UserResponseDto(user.getId(), user.getFirstName(), user.getMiddleName(), user.getLastName(),
                        user.getEmail(), user.getProfileImage(), user.getGender(), user.getMobileNumber(),user.getDateOfBirth(), user.getUserStatus()))
                .collect(Collectors.toList());
        return users;

    }

    @Override
    public List<ChatUserDto> findByNameOrEmailContainingIgnoreCase(UUID userId,String name) {
        List<ChatUserDto> users;
        List<User> userDetails;
        if(isNullorEmpty.apply(name))
            userDetails = userRepo.findAll();
        else
            userDetails = userRepo.
                    findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndMiddleNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase( 1,name,1, name, 1,name);
        users = userDetails.stream()
                .map(user ->
                {
               /*     List<ConversationParticipant> conversationParticipants = conversationParticipantRepo.findByUserId(userId);

                  List<ConversationParticipant> conversationParticipantSingle=conversationParticipants.stream().
                          filter(c -> c.getConversation().getConversationType() == ConversationType.Single).collect(Collectors.toList());

                  System.out.println(conversationParticipantSingle);
                  if(conversationParticipantSingle.size() > 0) {
                      List<ConversationParticipant> conversationParticipantsSingle1 = user.getConversationParticipants().stream().
                              filter(f -> f.getConversation().getConversationType() == ConversationType.Single).collect(Collectors.toList());

                      System.out.println("conversationParticipantsSingle1=========");
                      System.out.println(conversationParticipantsSingle1);
                  }
                 */
                    Long count = 0L;
                    UUID conversationId = null;
                    if(!userId.equals(user.getId())){
                        conversationId = conversationParticipantRepo.findBySingleConversationIdExist(userId,user.getId());
                     }
                    if(conversationId != null) {
                    ConversationParticipant conversationParticipant= conversationParticipantRepo.findByConversationIdAndUserId(conversationId,user.getId());
                         count = conversationParticipant.getConversationReadReceipts().stream().filter(p -> p.getMessageStatus() == MessageStatus.Send).count();
                    }

                    ChatUserDto chatUserDto = new ChatUserDto();
                    chatUserDto.setId(user.getId());
                    chatUserDto.setFirstName(user.getFirstName());
                    chatUserDto.setMiddleName(user.getMiddleName());
                    chatUserDto.setLastName(user.getLastName());
                    chatUserDto.setEmail(user.getEmail());
                    chatUserDto.setProfileImage(user.getProfileImage());
                    chatUserDto.setGender(user.getGender());
                    chatUserDto.setMobileNumber(user.getMobileNumber());
                    chatUserDto.setDateOfBirth(user.getDateOfBirth());
                    chatUserDto.setUserStatus(user.getUserStatus());
                    chatUserDto.setConversationId(conversationId);
                    chatUserDto.setUnreadMessageCount(Math.toIntExact(count));
                    return chatUserDto;
                }).collect(Collectors.toList());
        return users;
    }

    @Override
    public Pagination findByNameAndStatus(Integer status, String search, Pageable pageable) {
        Page<User> users = searchUser(status,search, pageable);
        Long totalElements = users.getTotalElements();
        List<UserResponseDto> userResponseDtoList = users.stream().map(
            user -> new UserResponseDto(user.getId(), user.getFirstName(), user.getMiddleName(), user.getLastName(),
                    user.getEmail(), user.getProfileImage(), user.getGender(), user.getMobileNumber(),user.getDateOfBirth(), user.getUserStatus()))
                .collect(Collectors.toList());

        return new Pagination(userResponseDtoList, totalElements.intValue());

    }

    private Page<User> searchUser ( Integer status, String search, Pageable pageable){
        if(isNullorEmpty.apply(search) && status == null ){
            return userRepo.findAll(pageable);
        }
        else if(isNullorEmpty.apply(search) && status != null){
            return userRepo.findByStatus(status, pageable);
        }
        else if(!isNullorEmpty.apply(search) && status != null){
            return  userRepo.findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndMiddleNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase(status, search, status, search, status, search, pageable);
        }
        return userRepo.findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndMiddleNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase(1,search, 1, search,1, search, pageable);
    }



}
