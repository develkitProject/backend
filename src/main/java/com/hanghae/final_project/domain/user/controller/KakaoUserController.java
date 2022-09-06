package com.hanghae.final_project.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.final_project.domain.user.service.KakaoUserService;
import com.hanghae.final_project.domain.user.service.UserService;
import com.hanghae.final_project.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoUserController {


    private final KakaoUserService kakaoUserService;

    @GetMapping("/user/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException,JsonProcessingException {
        return  kakaoUserService.kakaoLogin(code,response);
    }
}
