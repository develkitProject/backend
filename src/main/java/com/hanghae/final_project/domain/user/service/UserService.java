package com.hanghae.final_project.domain.user.service;

import com.hanghae.final_project.domain.user.dto.request.SignupDto;
import com.hanghae.final_project.domain.user.dto.request.UserProfileDto;
import com.hanghae.final_project.domain.user.dto.response.LoginDto;
import com.hanghae.final_project.domain.user.image.S3UploaderService;
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

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final SignupValidator signupValidator;

    private final S3UploaderService uploaderService;

    private final BCryptPasswordEncoder passwordEncoder;

    //일반회원 (not social) 회원가입
    public ResponseEntity<?> standardSignup(SignupDto signupDto) {

        signupValidator.checkUserInfoValidation(signupDto);

        checkDuplicate(signupDto.getUsername());

        User userInfo = User.of(signupDto,passwordEncoder);

        userRepository.save(userInfo);

        return new ResponseEntity<>(ResponseDto.success(LoginDto
                .builder().username(signupDto.getUsername()).build()),
                HttpStatus.OK);
    }

    //이메일 중복체크
    public ResponseEntity<?> checkEmail(SignupDto signupDto) {
        checkDuplicate(signupDto.getUsername());
        return new ResponseEntity<>(ResponseDto.success(null),HttpStatus.OK);
    }


    //유저 프로필 정보 변경
    /*
    *    유저 정보 값이 모두 있을 경우                ->    profileImage & 닉네임 변경
    *    유저 nickname의 값이 없을 경우.             ->    프로필 이미지만 변경
    *    유저 이미지 string 값이 없을 경우.          ->    유저 닉네임만 변경
    *    유저 이미지(string),이미지 모두 없을 경우    ->    BadRequest
    * */
    public ResponseEntity<?> changeProfile(User user, UserProfileDto userProfileDto) throws IOException {

        //유저정보 있는지 DB확인
        User userInfo=userRepository.findByUsername(user.getUsername())
                .orElseThrow(()->new RestApiException(CustomErrorCode.USER_NOT_EXIST));

        //nickname 변경할 경우
        if(userProfileDto.getNickname()!=null){
            userInfo.updateNickname(userProfileDto.getNickname());
        }

        //이미지 변경할 경우
        if(userProfileDto.getProfileImageUrl()!=null){
            //기존 이미지 아마존 S3에서 삭제.
            uploaderService.deleteImage(user.getProfileImage(),"user");
            //새로운 이미지 정보를 S3에 올리기
            String imageUrl=uploaderService.upload(userProfileDto.getProfileImageUrl(),"user");
            userInfo.updateProfileImage(imageUrl);
        }
        return new ResponseEntity<>(ResponseDto.success(null),HttpStatus.OK);
    }


    //이메일 중복 체크 로직 (회원가입 및 Front 중복체크 버튼)
    private void checkDuplicate(String username){

        Optional<User> found = userRepository.findByUsername(username);

        if (found.isPresent()) {
            throw new RestApiException(CustomErrorCode.INVALID_PARAMETER,"중복된 아이디입니다.");
        }
    }

}
