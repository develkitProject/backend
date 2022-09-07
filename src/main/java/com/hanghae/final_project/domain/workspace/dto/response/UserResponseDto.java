package com.hanghae.final_project.domain.workspace.dto.response;

import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.user.model.UserSocialEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Users user;

    public static UserResponseDto createResponseDto(User user) {
        return new UserResponseDto(new Users(user));
    }

    @Getter
    @AllArgsConstructor
    static class Users {
        private Long id;
        private String username;
        private String nickname;
        private String profileImage;
        private UserSocialEnum social;

        public Users(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.profileImage = user.getProfileImage();
            this.social = user.getSocial();
        }
    }
}
