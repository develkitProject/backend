package com.hanghae.final_project.api.workspace.dto.response;

import com.hanghae.final_project.domain.model.User;
import com.hanghae.final_project.domain.model.UserSocialEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Users user;

    public static UserResponseDto createResponseDto(User user,String workspaceCreator) {
        return new UserResponseDto(new Users(user,workspaceCreator));
    }

    @Getter
    @AllArgsConstructor
    static class Users {
        private Long id;
        private String username;
        private String nickname;
        private String profileImage;
        private UserSocialEnum social;

        private String workspaceCreator;

        public Users(User user,String workspaceCreator) {
            this.id = user.getId();
            this.workspaceCreator=workspaceCreator;
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.profileImage = user.getProfileImage();
            this.social = user.getSocial();
        }
    }
}
