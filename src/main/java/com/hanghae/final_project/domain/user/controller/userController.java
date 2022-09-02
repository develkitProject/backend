package com.hanghae.final_project.domain.user.controller;

import com.hanghae.final_project.domain.user.dto.request.SignupDto;
import com.hanghae.final_project.domain.user.service.UserService;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class userController {

    private final UserService userService;

    @PostMapping("/api/members/signup")
    public ResponseEntity<?> registerStandardUser(@Valid @RequestBody SignupDto signupDto, Errors errors){
        if (errors.hasErrors()) {
            log.info("error : {}", errors.getFieldError().getDefaultMessage());
            throw new RequestException(ErrorCode.USER_INFO_NOT_FORMATTED, errors.getFieldError().getDefaultMessage());
        }
        log.info("요청 메소드 [POST] /api/members/signup");
        return userService.standardSignup(signupDto);
    }

}
