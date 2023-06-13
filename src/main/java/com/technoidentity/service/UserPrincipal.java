package com.technoidentity.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.technoidentity.entity.Role;
import com.technoidentity.entity.User;
import com.technoidentity.enums.Gender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class UserPrincipal implements OAuth2User, UserDetails {
    private UUID id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String profileImage;

    @JsonIgnore
    private String password;
    private String mobileNumber;

    private Gender gender;
    private Role role;

    private Date dateOfBirth;


    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(UUID id, String firstName, String middleName, String lastName, String email, String password, String profileImage, String mobileNumber, Gender gender, Role role,Date dateOfBirth,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.mobileNumber =mobileNumber;
        this.gender = gender;
        this.role = role;
        this.dateOfBirth=dateOfBirth;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        return new UserPrincipal(
                user.getId(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getProfileImage(),
                user.getMobileNumber(),
                user.getGender(),
                user.getRole(),
                user.getDateOfBirth(),
                Collections.emptyList());
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public UUID getId() {
        return id;
    }



    public String getEmail() {
        return email;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public Role getRole() {
        return role;
    }

    public Date getDateOfBirth() {return dateOfBirth;}

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


}
