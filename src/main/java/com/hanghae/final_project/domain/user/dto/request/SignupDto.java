package com.hanghae.final_project.domain.user.dto.request;


import lombok.*;

@Getter @Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SignupDto {

    private String username;
    private String password;
    private String nickname;
}
