package com.hanghae.final_project.api.user.dto.request;



import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SignupDto {

    public static final String STANDARD_IMAGE_ROUTE="https://hosunghan.s3.ap-northeast-2.amazonaws.com/user/basic_profile_img.PNG";

    @NotBlank(message = "아이디는 필수 입력 값입니다")
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$" ,message = "올바른 이메일 형식이 아닙니다")
    private String username;

    @NotBlank(message = "이름은 필수 입력 값입니다")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[~!@#$%^&*()])[A-Za-z0-9~!@#$%^&*()]{8,20}$", message = "숫자와 문자, 특수문자를 포함한 8-20자리 입력해주세요")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다")
    @Pattern(regexp = "^[0-9a-zA-Zㄱ-ㅎ가-힣]{2,8}$",message = "닉네임은 2~8글자로 등록가능합니다.")
    private String nickname;

    private String profileImage;
}
