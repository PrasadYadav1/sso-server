package com.technoidentity.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import com.technoidentity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends SharedModel implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "role_id")
    private UUID roleId;


    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "role_id",nullable = false,insertable = false, updatable = false)
    private Role role;

    @Column(nullable = false,name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(nullable = false,name = "last_name")
    private String lastName;

    @Email
    @Column(nullable = false)
    private String email;


    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(nullable = false,name = "email_verified")
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @Column(name = "date_of_birth", columnDefinition= "DATE")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(name = "provider_id")
    private String providerId;

    private Gender gender;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private List<ConversationParticipant> conversationParticipants = new ArrayList<>();

}
