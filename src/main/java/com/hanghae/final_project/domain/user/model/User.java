package com.hanghae.final_project.domain.user.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.final_project.domain.user.dto.request.SignupDto;
import com.hanghae.final_project.domain.workspace.model.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="users")
public class User extends Timestamped {

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



    public static User of(SignupDto signupDto, BCryptPasswordEncoder passwordEncoder){
        return User.builder()
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
