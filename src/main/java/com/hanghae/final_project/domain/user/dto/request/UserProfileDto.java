package com.hanghae.final_project.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserProfileDto {

    private String profileImageUrl;
    private String nickname;
}
