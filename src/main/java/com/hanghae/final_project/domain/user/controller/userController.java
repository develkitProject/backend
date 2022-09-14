package com.hanghae.final_project.domain.user.controller;

import com.hanghae.final_project.domain.user.dto.request.SignupDto;
import com.hanghae.final_project.domain.user.dto.request.UserProfileDto;
import com.hanghae.final_project.domain.user.service.UserService;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class userController {

    private final UserService userService;

    @PostMapping("/api/members/signup")
    public ResponseEntity<?> registerStandardUser(@Valid @RequestBody SignupDto signupDto, Errors errors){
        if (errors.hasErrors()) {
            log.info("error : {}", errors.getAllErrors().get(0).getDefaultMessage());
            throw new RequestException(ErrorCode.USER_INFO_NOT_FORMATTED, errors.getAllErrors().get(0).getDefaultMessage());
        }
        log.info("요청 메소드 [POST] /api/members/signup");
        return userService.standardSignup(signupDto);
    }
    @PostMapping("/api/members/email")
    public ResponseEntity<?>  checkEmailDuplicate(@RequestBody SignupDto signupDto){
        return userService.checkEmail(signupDto);
    }

    @GetMapping("/api/members/profile")
    public ResponseEntity<?> getUserProfile( @AuthenticationPrincipal UserDetailsImpl userDetails){

       return userService.getProfile(userDetails.getUser());
    }

    @PutMapping("/api/members/profile")
    public ResponseEntity<?> changeUserProfile(@RequestBody UserProfileDto userprofileDto,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return userService.changeProfile(userDetails.getUser(),userprofileDto);
    }



}
