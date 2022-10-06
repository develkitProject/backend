package com.hanghae.final_project.infra.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hanghae.final_project.api.user.dto.response.KakaoSocialDto;
import com.hanghae.final_project.api.user.dto.response.LoginDto;
import com.hanghae.final_project.domain.model.User;
import com.hanghae.final_project.domain.model.UserSocialEnum;
import com.hanghae.final_project.domain.repository.user.UserRepository;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import com.hanghae.final_project.global.config.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${kakao.login.admin-key}")
    private String APP_ADMIN_KEY;
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    //카카오로그인
    public ResponseDto<LoginDto> kakaoLogin(String code, HttpServletResponse response) throws IOException {

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoSocialDto kakaoSocialDto = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUser(kakaoSocialDto);

        // 4. 토큰 발급
        kakaoLoginAccess(kakaoUser, response);


        return ResponseDto.success(
                        LoginDto.builder()
                                .username(kakaoUser.getUsername())
                                .profileImage(kakaoUser.getProfileImage())
                                .build()
                );

    }

    private void kakaoLoginAccess(User kakaoUser, HttpServletResponse response) {

        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        String token = JwtTokenUtils.generateJwtToken(userDetails);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);

    }

    private User registerKakaoUser(KakaoSocialDto kakaoSocialDto) {

        // User 정보있는지 확인
        User kakaoUser = userRepository.findByUsername(kakaoSocialDto.getUsername()).orElse(null);

        // User 정보가 없으면 회원가입 시키기
        if (kakaoUser == null) {
            String password = UUID.randomUUID().toString();

            kakaoUser = User.builder()
                    .kakaoId(kakaoSocialDto.getKakaoId())
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


        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    private KakaoSocialDto getKakaoUserInfo(String accessToken) throws IOException {
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

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper
                .readTree(responseBody);

        Long kakaoId = jsonNode.get("id").asLong();

        String nickname = jsonNode
                .get("properties")
                .get("nickname").asText();

        String profileImage = jsonNode
                .get("properties")
                .get("profile_image").asText();

        String email = jsonNode
                .get("kakao_account")
                .get("email").asText();

        return KakaoSocialDto.builder()
                .username(email)
                .nickname(nickname)
                .profileImage(profileImage)
                .kakaoId(kakaoId)
                .build();
    }

    //카카오 회원일 경우, application 연결 끊기 과정 진행
    public void signOutKakaoUser(User user) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + APP_ADMIN_KEY);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", user.getKakaoId().toString());

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/unlink",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        log.info("회원탈퇴 한 유저의 kakaoId : {}", response.getBody());
    }
}
