package com.hanghae.final_project.api.user.dto.response;

import com.hanghae.final_project.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDto {

    private String username;
    private String profileImageUrl;
    private String nickname;
    private String createdAt;
    private String documentNum;

    public static ProfileResponseDto of(User user, Long documentNum) {
        return ProfileResponseDto.builder()
                .documentNum(documentNum.toString())
                .createdAt(user.getCreatedAt())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImage())
                .build();
    }
}
