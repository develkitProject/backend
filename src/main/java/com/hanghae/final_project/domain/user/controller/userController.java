package com.hanghae.final_project.domain.user.controller;

import com.hanghae.final_project.domain.user.dto.request.SignupDto;
import com.hanghae.final_project.domain.user.dto.request.UserProfileDto;
import com.hanghae.final_project.domain.user.dto.response.LoginDto;
import com.hanghae.final_project.domain.user.service.UserService;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Api(tags = "Login")
@Slf4j
@RequiredArgsConstructor
@RestController
public class userController {

    private final UserService userService;

    @ApiOperation(value = "회원가입", notes = "일반 회원가입")
    @PostMapping("/api/members/signup")
    public ResponseDto<LoginDto> registerStandardUser(@Valid @RequestBody SignupDto signupDto, Errors errors){
        if (errors.hasErrors()) {
            log.info("error : {}", errors.getAllErrors().get(0).getDefaultMessage());
            throw new RequestException(ErrorCode.USER_INFO_NOT_FORMATTED, errors.getAllErrors().get(0).getDefaultMessage());
        }
        log.info("요청 메소드 [POST] /api/members/signup");
        ResponseDto<LoginDto> loginDto=userService.standardSignup(signupDto);
        userService.setGuestWorkspace(loginDto.getData().getUsername());
        return loginDto;
    }

    @ApiOperation(value = "이메일 중복체크", notes = "닉네임은 중복 허용")
    @PostMapping("/api/members/email")
    public ResponseEntity<?>  checkEmailDuplicate(@RequestBody SignupDto signupDto){
        return userService.checkEmail(signupDto);
    }

    @ApiOperation(value = "마이페이지 조회", notes = "마이페이지 조회")
    @GetMapping("/api/members/profile")
    public ResponseEntity<?> getUserProfile( @AuthenticationPrincipal UserDetailsImpl userDetails){

       return userService.getProfile(userDetails.getUser());
    }

    @ApiOperation(value = "마이페이지 수정", notes = "마이페이지 수정(닉네임, 프로필 사진)")
    @PutMapping("/api/members/profile")
    public ResponseEntity<?> changeUserProfile(@RequestBody UserProfileDto userprofileDto,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return userService.changeProfile(userDetails.getUser(),userprofileDto);
    }

    @ApiOperation(value = "회원탈퇴 ", notes ="회원탈퇴")
    @DeleteMapping("/api/members/signout")
    public ResponseDto<Boolean> signOutMember(@AuthenticationPrincipal UserDetailsImpl userDetails){

        return  userService.signOut(userDetails.getUser());
    }

    @ApiOperation(value="게스트 자동회원가입",notes="회원가입 및 로그인 처리")
    @GetMapping("/api/members/guest")
    public ResponseDto<LoginDto> guestSignupAndSignIn(){
        log.info("게스트 회원가입 진행 ");
        ResponseDto<LoginDto> loginDto = userService.guestSignup();
        userService.setGuestWorkspace(loginDto.getData().getUsername());
        return loginDto;
    }


}
