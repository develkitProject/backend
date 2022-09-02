package com.hanghae.final_project.domain.user.dto.request;



import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignupDto {

    public static final String STANDARD_IMAGE_ROUTE="https://hosunghan.s3.ap-northeast-2.amazonaws.com/user/basic_profile_img.PNG";

    private String username;
    private String password;
    private String nickname;
    private String profileImage;
}
