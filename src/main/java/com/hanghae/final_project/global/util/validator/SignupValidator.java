package com.hanghae.final_project.global.util.validator;

import com.hanghae.final_project.api.user.dto.request.SignupDto;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class SignupValidator {

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$";

    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[~!@#$%^&*()])" +
                    "[A-Za-z0-9~!@#$%^&*()]" +
                    "{8,20}$";

    private static final String NICKNAME_PATTERN =
            "^[0-9a-zA-Zㄱ-ㅎ가-힣]{2,8}$" ;


    public void checkUserInfoValidation(SignupDto signupDto){
        if ( signupDto.getUsername()==null || !Pattern.matches(EMAIL_PATTERN,signupDto.getUsername())  ){
            throw new IllegalArgumentException("Email 정보가 유효하지 않습니다");
        }
        if( signupDto.getPassword()==null || !Pattern.matches(PASSWORD_PATTERN,signupDto.getPassword()) ){
            throw new IllegalArgumentException("Password 정보가 유효하지 않습니다");
        }
        if( signupDto.getNickname()==null || !Pattern.matches(NICKNAME_PATTERN,signupDto.getNickname())  ){
            throw new IllegalArgumentException("닉네임 정보가 유효하지 않습니다");
        }
    }

}
