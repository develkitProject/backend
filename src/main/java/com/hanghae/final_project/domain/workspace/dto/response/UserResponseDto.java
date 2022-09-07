package com.hanghae.final_project.domain.workspace.dto.response;

import com.hanghae.final_project.domain.user.dto.request.SignupDto;
import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.user.model.UserSocialEnum;
import com.hanghae.final_project.global.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static com.hanghae.final_project.domain.user.dto.request.SignupDto.STANDARD_IMAGE_ROUTE;

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
