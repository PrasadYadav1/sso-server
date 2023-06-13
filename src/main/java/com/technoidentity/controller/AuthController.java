package com.technoidentity.controller;


import com.technoidentity.dto.*;
import com.technoidentity.entity.User;
import com.technoidentity.exception.BadRequestException;
import com.technoidentity.security.TokenProvider;
import com.technoidentity.service.UserPrincipal;
import com.technoidentity.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@Api(tags = "Authentication")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignIn signIn) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signIn.getEmail(),
                        signIn.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        UserDto userDto = new UserDto(userDetails.getId(), userDetails.getRole().getId(), userDetails.getRole().getName(),
                userDetails.getFirstName(), userDetails.getMiddleName(), userDetails.getLastName(), userDetails.getEmail(), userDetails.getProfileImage(), userDetails.getGender(), userDetails.getMobileNumber(),userDetails.getDateOfBirth());
        return ResponseEntity.ok(new SignInResponseDto(token, "Bearer", userDto));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUp signUp) {
        if (userService.existsByEmail(signUp.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        User result = userService.signUp(signUp);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully"));
    }

}

