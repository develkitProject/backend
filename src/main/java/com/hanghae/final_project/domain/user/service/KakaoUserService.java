package com.hanghae.final_project.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hanghae.final_project.domain.user.dto.response.KakaoSocialDto;
import com.hanghae.final_project.domain.user.dto.response.LoginDto;
import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.user.model.UserSocialEnum;
import com.hanghae.final_project.domain.user.repository.UserRepository;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import com.hanghae.final_project.global.config.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.hanghae.final_project.global.config.security.handler.AuthenticationSuccessHandler.AUTH_HEADER;
import static com.hanghae.final_project.global.config.security.handler.AuthenticationSuccessHandler.TOKEN_TYPE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoUserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;



    public ResponseEntity<?> kakaoLogin(String code, HttpServletResponse response) throws IOException,JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoSocialDto kakaoSocialDto = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User kakaoUser=registerKakaoUser(kakaoSocialDto);

        // 4. 토큰 발급
        kakaoLoginAccess(kakaoUser,response);

        log.info("카카오 로그인 완료 : {}",kakaoUser.getUsername());

        return new ResponseEntity<>(
                ResponseDto.success(
                        LoginDto.builder()
                                .username(kakaoUser.getUsername())
                                .profileImage(kakaoUser.getProfileImage())
                                .build()
                ), HttpStatus.OK);
        //response.addHeader("Authorization", "BEARER eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJFWFBJUkVEX0RBVEUiOjE2NjE0NzkxNTgsImlzcyI6IklTUyIsIlVTRVJfTkFNRSI6IndpbnNvbWVkMzIifQ.Ma3yRP88ORqs4FUZBdRf-telMnm7o_lnKKJG2rc0qLo");
    }

    private void kakaoLoginAccess(User kakaoUser, HttpServletResponse response) {

        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        String token= JwtTokenUtils.generateJwtToken(userDetails);
        response.addHeader(AUTH_HEADER,TOKEN_TYPE+" "+token);

    }

    private User registerKakaoUser(KakaoSocialDto kakaoSocialDto) {

        // User 정보있는지 확인
        User kakaoUser = userRepository.findByUsername(kakaoSocialDto.getUsername()).orElse(null);

        // User 정보가 없으면 회원가입 시키기
        if (kakaoUser == null) {
            String password = UUID.randomUUID().toString();

            kakaoUser = User.builder()
                    .nickname(kakaoSocialDto.getNickname())
                    .password(encoder.encode(password))
                    .username(kakaoSocialDto.getUsername())
                    .social(UserSocialEnum.KAKAO)
                    .profileImage(kakaoSocialDto.getProfileImage())
                    .build();

            userRepository.save(kakaoUser);
        }

        return kakaoUser;

    }


    private String getAccessToken(String code) throws JsonProcessingException {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        log.info("kakao_login_code : "+code);
        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "c8cf862f6090965a35b740904db575b1");
        body.add("redirect_uri", "https://d-velkit.com/kakao");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();

        log.info(responseBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoSocialDto getKakaoUserInfo(String accessToken) throws IOException,JsonProcessingException {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        log.info("responseBody : " + responseBody);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper
                .readTree(responseBody);

        String nickname = jsonNode
                .get("properties")
                .get("nickname").asText();

        String profileImage = jsonNode
                .get("properties")
                .get("profile_image").asText();

        String email = jsonNode
                .get("kakao_account")
                .get("email").asText();

        log.info("nickname : " + nickname);
        log.info("email : " + email);

        return KakaoSocialDto.builder()
                .username(email)
                .nickname(nickname)
                .profileImage(profileImage)
                .build();

    }


}
