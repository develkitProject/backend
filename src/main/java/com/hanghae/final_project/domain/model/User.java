package com.hanghae.final_project.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.final_project.api.user.dto.request.SignupDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

import java.io.Serializable;

import static com.hanghae.final_project.api.user.dto.request.SignupDto.STANDARD_IMAGE_ROUTE;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="users")
public class User extends Timestamped implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profileImage;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserSocialEnum social;

    @Column
    private Long kakaoId;

    public static User of(SignupDto signupDto, BCryptPasswordEncoder passwordEncoder){
        return User.builder()
                .profileImage(STANDARD_IMAGE_ROUTE)
                .username(signupDto.getUsername())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .nickname(signupDto.getNickname())
                .social(UserSocialEnum.STANDARD)
                .build();
    }

    public void updateNickname(String nickname) {
        this.nickname=nickname;
    }
    public void updateProfileImage(String profileImage){
        this.profileImage=profileImage;
    }
}
