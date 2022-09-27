package com.hanghae.final_project.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.final_project.domain.user.dto.response.LoginDto;
import com.hanghae.final_project.domain.user.service.KakaoUserService;
import com.hanghae.final_project.domain.user.service.UserService;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(tags = "KakaoUser Login")
@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoUserController {


    private final KakaoUserService kakaoUserService;

    private final UserService userService;

    @ApiOperation(value = "카카오톡 로그인", notes = "카카오톡 로그인")
    @GetMapping("/user/kakao/callback")
    public ResponseDto<LoginDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException, JsonProcessingException {
        ResponseDto<LoginDto> loginDto = kakaoUserService.kakaoLogin(code, response);
        userService.setGuestWorkspace(loginDto.getData().getUsername());
        return loginDto ;
    }
}
