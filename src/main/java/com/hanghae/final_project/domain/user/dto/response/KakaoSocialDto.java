package com.hanghae.final_project.domain.user.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoSocialDto {

    private String username;
    private String nickname;
    private String profileImage;
}
