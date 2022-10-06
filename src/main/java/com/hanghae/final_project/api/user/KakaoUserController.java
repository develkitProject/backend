package com.hanghae.final_project.api.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.final_project.api.user.dto.response.LoginDto;
import com.hanghae.final_project.infra.kakao.KakaoUserService;
import com.hanghae.final_project.service.user.UserService;
import com.hanghae.final_project.global.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseDto<LoginDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException{
        ResponseDto<LoginDto> loginDto = kakaoUserService.kakaoLogin(code, response);
        userService.setGuestWorkspace(loginDto.getData().getUsername());
        return loginDto ;
    }
}
