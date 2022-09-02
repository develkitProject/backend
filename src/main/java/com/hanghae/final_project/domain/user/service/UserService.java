package com.hanghae.final_project.domain.user.service;

import com.hanghae.final_project.domain.user.dto.request.SignupDto;
import com.hanghae.final_project.domain.user.dto.response.LoginDto;
import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.user.repository.UserRepository;
import com.hanghae.final_project.domain.user.validation.SignupValidator;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.error.errorcode.CustomErrorCode;
import com.hanghae.final_project.global.error.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final SignupValidator signupValidator;

    private final BCryptPasswordEncoder passwordEncoder;
    public ResponseEntity<?> standardSignup(SignupDto signupDto) {


        signupValidator.checkUserInfoValidation(signupDto);

        Optional<User> found = userRepository.findByUsername(signupDto.getUsername());

        if (found.isPresent()) {
            throw new RestApiException(CustomErrorCode.INVALID_PARAMETER,"중복된 아이디입니다.");
        }
        User userInfo = User.of(signupDto,passwordEncoder);

        userRepository.save(userInfo);

        return new ResponseEntity<>(ResponseDto.success(LoginDto
                .builder().username(signupDto.getUsername()).build()),
                HttpStatus.OK);
    }
}
