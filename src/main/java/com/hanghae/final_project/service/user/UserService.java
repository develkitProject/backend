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

    private final WorkSpaceRepository workSpaceRepository;
    private final S3UploaderService uploaderService;
    private final ChatRedisCacheService chatRedisCacheService;
    private final KakaoUserService kakaoUserService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final DocumentRepository documentRepository;
    private final WorkSpaceUserRepository workSpaceUserRepository;

    //???????????? (not social) ????????????
    public ResponseDto<LoginDto> standardSignup(SignupDto signupDto) {

        checkDuplicate(signupDto.getUsername());

        User userInfo = User.of(signupDto, passwordEncoder);

        userRepository.save(userInfo);

        return ResponseDto.success(LoginDto
                .builder()
                .username(signupDto.getUsername())
                .build());
    }

    //????????? ????????????
    public ResponseDto<Boolean> checkEmail(SignupDto signupDto) {
        Optional<User> found = userRepository.findByUsername(signupDto.getUsername());

        if (found.isPresent()) {
           return ResponseDto.fail(ErrorCode.USER_LOGINID_NOT_FOUND_404.name()
                    , ErrorCode.USER_LOGINID_NOT_FOUND_404.getMessage());
        }

        return ResponseDto.success(true);
    }


    //?????? ???????????? ??????
    public ResponseEntity<?> getProfile(User user) {

        checkUsername(user.getUsername());

        return new ResponseEntity<>(ResponseDto.success(ProfileResponseDto.of(user, documentRepository.countDocumentByUser(user))), HttpStatus.OK);

    }

    /*
     *    ?????? ?????? ?????? ?????? ?????? ??????                ->    profileImage & ????????? ??????
     *    ?????? nickname??? ?????? ?????? ??????.             ->    ????????? ???????????? ??????
     *    ?????? ????????? string ?????? ?????? ??????.          ->    ?????? ???????????? ??????
     *    ?????? ?????????(string),????????? ?????? ?????? ??????    ->    BadRequest
     * */
    public ResponseDto<Boolean> changeProfile(User user, UserProfileDto userProfileDto) throws IOException {

        //???????????? ????????? DB??????
        User userInfo = checkUsername(user.getUsername());

        //nickname ????????? ??????
        if (userProfileDto.getNickname() != null) {

            userInfo.updateNickname(userProfileDto.getNickname());
            chatRedisCacheService.changeUserCachingNickname(user.getUsername(), userInfo.getNickname());
        }

        //????????? ????????? ??????
        if (userProfileDto.getProfileImageUrl() != null) {

            uploaderService.deleteFiles(user.getProfileImage(), "user");

            String imageUrl = uploaderService.uploadBase64Image(userProfileDto.getProfileImageUrl(), "user");
            userInfo.updateProfileImage(imageUrl);

        }
        return ResponseDto.success(true);
    }

    // ????????????
    public ResponseDto<Boolean> signOut(User user) {

        if (!user.getProfileImage().contains(STANDARD_IMAGE_ROUTE)) {
            uploaderService.deleteFiles(user.getProfileImage(), "user");
        }

        //kakao ?????? ??????
        if (user.getSocial().equals(UserSocialEnum.KAKAO))
            kakaoUserService.signOutKakaoUser(user);

        //Redis ????????? ?????? ??????
        chatRedisCacheService.deleteUserCahchingNickname(user.getUsername());

        // DB?????? USER ??????
        userRepository.delete(user);

        return ResponseDto.success(true);
    }
    //??????????????????
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
    //Guest Workspace ??????
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
    private User checkUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RequestException(ErrorCode.USER_NOT_EXIST));
    }
    //????????? ?????? ?????? ?????? (???????????? ??? Front ???????????? ??????)
    private void checkDuplicate(String username) {

        Optional<User> found = userRepository.findByUsername(username);

        if (found.isPresent()) {
            throw new RequestException(ErrorCode.USER_LOGINID_DUPLICATION_409);
        }
    }
}
