package com.hanghae.final_project.domain.user.controller;

import com.hanghae.final_project.domain.user.dto.request.SignupDto;
import com.hanghae.final_project.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class userController {

    private final UserService userService;

    @PostMapping("/api/members/signup")
    public ResponseEntity<?> registerStandardUser(@RequestBody SignupDto signupDto){
        log.info("요청 메소드 [POST] /api/members/signup");
        return userService.standardSignup(signupDto);
    }

}
