package com.hanghae.final_project.domain.user.controller;

import com.hanghae.final_project.domain.user.dto.request.SignupDto;
import com.hanghae.final_project.domain.user.dto.request.UserProfileDto;
import com.hanghae.final_project.domain.user.service.UserService;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import com.hanghae.final_project.global.security.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@Api(tags = "User")
@Slf4j
@RequiredArgsConstructor
@RestController
public class userController {

    private final UserService userService;

    @ApiOperation(value = "회원가입", notes = "아이디, 비밀번호 유효성 검사 포함됨")
    @PostMapping("/api/members/signup")
    public ResponseEntity<?> registerStandardUser(@Valid @RequestBody SignupDto signupDto, Errors errors){
        if (errors.hasErrors()) {
            log.info("error : {}", errors.getAllErrors().get(0).getDefaultMessage());
            throw new RequestException(ErrorCode.USER_INFO_NOT_FORMATTED, errors.getAllErrors().get(0).getDefaultMessage());
        }
        log.info("요청 메소드 [POST] /api/members/signup");
        return userService.standardSignup(signupDto);
    }

    @ApiOperation(value = "이메일 중복확인", notes = "이메일 중복 허용하지않음")
    @PostMapping("/api/members/email")
    public ResponseEntity<?>  checkEmailDuplicate(@RequestBody SignupDto signupDto){
        return userService.checkEmail(signupDto);
    }

    @ApiOperation(value = "회원정보 조회", notes = "닉네임(중복허용), 프로필사진")
    @GetMapping("/api/members/profile")
    public ResponseEntity<?> getUserProfile( @AuthenticationPrincipal UserDetailsImpl userDetails){

       return userService.getProfile(userDetails.getUser());
    }

    @ApiOperation(value = "회원정보 수정", notes = "회원가입 후 기본이미지 변경")
    @PostMapping("/api/members/profile")
    public ResponseEntity<?> changeUserProfile(@RequestBody UserProfileDto userprofileDto,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return userService.changeProfile(userDetails.getUser(),userprofileDto);
    }



}
