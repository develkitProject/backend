package com.hanghae.final_project.domain.user.dto.response;

import com.hanghae.final_project.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResProfileDto {

    private String username;
    private String profileImageUrl;
    private String nickname;

    public static ResProfileDto of(User user) {
        return ResProfileDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImage())
                .build();
    }
}
