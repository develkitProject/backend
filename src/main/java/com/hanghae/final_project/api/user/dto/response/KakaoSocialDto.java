package com.hanghae.final_project.api.user.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoSocialDto {

    private Long kakaoId;
    private String username;
    private String nickname;
    private String profileImage;
}
