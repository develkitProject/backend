package com.hanghae.final_project.service.user;

import com.hanghae.final_project.infra.kakao.KakaoUserService;
import com.hanghae.final_project.service.chat.ChatRedisCacheService;
import com.hanghae.final_project.api.user.dto.request.SignupDto;
import com.hanghae.final_project.api.user.dto.request.UserProfileDto;
import com.hanghae.final_project.api.user.dto.response.LoginDto;
import com.hanghae.final_project.api.user.dto.response.ProfileResponseDto;
import com.hanghae.final_project.domain.model.User;
import com.hanghae.final_project.domain.model.UserSocialEnum;
import com.hanghae.final_project.domain.repository.user.UserRepository;
import com.hanghae.final_project.domain.model.WorkSpace;
import com.hanghae.final_project.domain.model.WorkSpaceUser;
import com.hanghae.final_project.domain.repository.workspace.DocumentRepository;
import com.hanghae.final_project.domain.repository.workspace.WorkSpaceRepository;
import com.hanghae.final_project.domain.repository.workspace.WorkSpaceUserRepository;
import com.hanghae.final_project.infra.s3fileupload.S3UploaderService;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static com.hanghae.final_project.api.user.dto.request.SignupDto.STANDARD_IMAGE_ROUTE;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
//    private final SignupValidator signupValidator;

    private final WorkSpaceRepository workSpaceRepository;
    private final S3UploaderService uploaderService;

    private final ChatRedisCacheService chatRedisCacheService;
    private final KakaoUserService kakaoUserService;
    private final BCryptPasswordEncoder passwordEncoder;

    private final DocumentRepository documentRepository;

    private final WorkSpaceUserRepository workSpaceUserRepository;

    //일반회원 (not social) 회원가입
    public ResponseDto<LoginDto> standardSignup(SignupDto signupDto) {

        checkDuplicate(signupDto.getUsername());

        User userInfo = User.of(signupDto, passwordEncoder);

        userRepository.save(userInfo);

        return ResponseDto.success(LoginDto
                .builder()
                .username(signupDto.getUsername())
                .build());
    }

    //이메일 중복체크
    public ResponseEntity<?> checkEmail(SignupDto signupDto) {
        Optional<User> found = userRepository.findByUsername(signupDto.getUsername());

        if (found.isPresent()) {
            return new ResponseEntity<>(ResponseDto.fail(
                    ErrorCode.USER_LOGINID_DUPLICATION_409.name(),
                    ErrorCode.USER_LOGINID_DUPLICATION_409.getMessage()),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(ResponseDto.success(null), HttpStatus.OK);
    }


    //유저 개인정보 조회
    public ResponseEntity<?> getProfile(User user) {

        checkUsername(user.getUsername());

        return new ResponseEntity<>(ResponseDto.success(ProfileResponseDto.of(user, documentRepository.countDocumentByUser(user))), HttpStatus.OK);

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
        User userInfo = checkUsername(user.getUsername());

        //nickname 변경할 경우
        if (userProfileDto.getNickname() != null) {

            userInfo.updateNickname(userProfileDto.getNickname());
            chatRedisCacheService.changeUserCachingNickname(user.getUsername(), userInfo.getNickname());
        }

        //이미지 변경할 경우
        if (userProfileDto.getProfileImageUrl() != null) {
            //기존 이미지 아마존 S3에서 삭제.
            uploaderService.deleteFiles(user.getProfileImage(), "user");
            //새로운 이미지 정보를 S3에 올리기
            String imageUrl = uploaderService.uploadBase64Image(userProfileDto.getProfileImageUrl(), "user");
            userInfo.updateProfileImage(imageUrl);
        }
        return new ResponseEntity<>(ResponseDto.success(null), HttpStatus.OK);
    }


    //이메일 중복 체크 로직 (회원가입 및 Front 중복체크 버튼)
    private void checkDuplicate(String username) {

        Optional<User> found = userRepository.findByUsername(username);

        if (found.isPresent()) {
            throw new RequestException(ErrorCode.USER_LOGINID_DUPLICATION_409);
        }
    }

    private User checkUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RequestException(ErrorCode.USER_NOT_EXIST));
    }

    // 회원탈퇴 진행.

    public ResponseDto<Boolean> signOut(User user) {
        //유저의 이미지 데이터 S3로부터 삭제

        if (!user.getProfileImage().contains(STANDARD_IMAGE_ROUTE)) {
            log.info("회원탈퇴시, 기본이미지가 아닐 경우, 해당 이미지 삭제");
            uploaderService.deleteFiles(user.getProfileImage(), "user");
        } else {
            log.info("회원탈퇴시, 기본이미지 일 경우 이미지 삭제 안함");
        }
        //kakao 유저 끊기
        if (user.getSocial().equals(UserSocialEnum.KAKAO))
            kakaoUserService.signOutKakaoUser(user);

        //Redis 닉네임 정보 삭제
        chatRedisCacheService.deleteUserCahchingNickname(user.getUsername());

        // DB에서 USER 제거
        userRepository.delete(user);
        return ResponseDto.success(true);
    }

    public ResponseDto<LoginDto> guestSignup() {

        Long id;
        User user = userRepository.findTopByOrderByIdDesc().orElse(null);

        if (user == null) {
            id = 1L;
        } else id = user.getId() + 1;
        SignupDto signupDto = SignupDto.builder()
                .username("Guest" + id + "@dvelkit.com")
                .nickname("Guest" + id)
                .password("Guest" + id + "!")
                .build();

        return standardSignup(signupDto);
    }

    public void setGuestWorkspace(String username) {


        User user = userRepository.findByUsername(username).orElse(null);
        WorkSpace workSpace1 = workSpaceRepository.findById(104L).orElse(null);
        WorkSpaceUser workSpaceUser = workSpaceUserRepository
                .findByUserAndWorkSpaceId(user, workSpace1.getId())
                .orElse(null);

        if(workSpaceUser==null){
            WorkSpaceUser workSpaceUser1 = WorkSpaceUser.builder()
                    .workSpace(workSpace1)
                    .user(user)
                    .build();

            WorkSpace workSpace2 = workSpaceRepository.findById(106L).orElse(null);

            WorkSpaceUser workSpaceUser2 = WorkSpaceUser.builder()
                    .workSpace(workSpace2)
                    .user(user)
                    .build();

            workSpaceUserRepository.save(workSpaceUser1);
            workSpaceUserRepository.save(workSpaceUser2);
        }


    }
}