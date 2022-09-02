package com.hanghae.final_project.domain.user.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
public class SignupDto {

    private String username;
    private String password;
    private String nickname;

}
