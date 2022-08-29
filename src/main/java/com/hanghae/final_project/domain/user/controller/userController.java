package com.hanghae.final_project.domain.user.controller;

import com.hanghae.final_project.domain.user.dto.request.SignupDto;
import com.hanghae.final_project.domain.user.dto.request.UserProfileDto;
import com.hanghae.final_project.domain.user.service.UserService;
import com.hanghae.final_project.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class userController {

    private final UserService userService;


    @PostMapping("/api/members/signup")
    public ResponseEntity<?> registerStandardUser(@RequestBody SignupDto signupDto){
        return userService.standardSignup(signupDto);
    }
    @PostMapping("/api/members/email")
    public ResponseEntity<?>  checkEmailDuplicate(@RequestBody SignupDto signupDto){
        return userService.checkEmail(signupDto);
    }

    @PostMapping("/api/members/profile")
    public ResponseEntity<?> changeUserProfile(@RequestBody UserProfileDto userprofileDto,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return userService.changeProfile(userDetails.getUser(),userprofileDto);
    }



}
