package com.hanghae.final_project.domain.workspace.service;

import com.hanghae.final_project.domain.chatting.repository.ChatRoomRepository;
import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.user.repository.UserRepository;
import com.hanghae.final_project.domain.workspace.dto.request.WorkSpaceUpdateReqeustDto;
import com.hanghae.final_project.domain.workspace.dto.request.WorkspaceFindRecentData;
import com.hanghae.final_project.domain.workspace.dto.request.WorkspaceRequestDto;
import com.hanghae.final_project.domain.workspace.dto.response.MainResponseDto;
import com.hanghae.final_project.domain.workspace.dto.response.UserResponseDto;
import com.hanghae.final_project.domain.workspace.dto.response.WorkSpaceInfoResponseDto;
import com.hanghae.final_project.domain.workspace.dto.response.WorkspaceResponseDto;
import com.hanghae.final_project.domain.workspace.model.*;
import com.hanghae.final_project.domain.workspace.repository.*;
import com.hanghae.final_project.global.util.image.S3UploaderService;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {
    private final WorkSpaceUserRepository workspaceUserRepository;
    private final WorkSpaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final S3UploaderService s3UploaderService;
    private final DocumentRepository documentRepository;
    private final NoticeRepository noticeRepository;

    private final ScheduleRepository scheduleRepository;

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ResponseDto<WorkspaceResponseDto> createWorkspace(WorkspaceRequestDto requestDto,
                                                             UserDetails userDetails) throws IOException {

        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        // 워크 스페이스를 생성하고, 자신의 정보를 넣어줌

        // 이미지가 올라와있지 않다면, workspaceImage를 기본값으로. 변경 x
        // 이미지가 올라와있다면, upload를 통해서

        String imgUrl = "https://hosunghan.s3.ap-northeast-2.amazonaws.com/workspace/workspaceimg.png";
        if (requestDto.getImage() != null && !requestDto.getImage().equals("")) {
            imgUrl = s3UploaderService.uploadBase64Image(requestDto.getImage(), "workspace");
        }
        WorkSpace workSpace = WorkSpace.of(requestDto, imgUrl, user);
        WorkSpace savedWorkspace = workspaceRepository.save(workSpace);

        WorkSpaceUser workSpaceUser = WorkSpaceUser.of(user, savedWorkspace);
        WorkSpaceUser savedWorkspaceUser = workspaceUserRepository.save(workSpaceUser);

        WorkspaceResponseDto responseDto = WorkspaceResponseDto.createResponseDto(savedWorkspaceUser.getWorkSpace(),null);

        return ResponseDto.success(responseDto);
    }

    // 참여한 모든 workspace 조회
    public ResponseDto<List<WorkspaceResponseDto>> getWorkspaces(UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        List<WorkSpaceUser> repositories = workspaceUserRepository.findAllByUser(user);

        List<WorkspaceResponseDto> responseDtos = repositories.stream()
                .map(workSpaceUser -> WorkspaceResponseDto
                        .createResponseDto(
                                workSpaceUser.getWorkSpace(),
                                findWorkspaceRecent(workSpaceUser.getWorkSpace())))
                .collect(Collectors.toList());

        return ResponseDto.success(responseDtos);
    }

    private WorkspaceFindRecentData findWorkspaceRecent(WorkSpace workspace) {
        Document document = documentRepository.findFirstByWorkSpaceIdOrderByCreatedAtDesc(workspace.getId())
                .orElse(Document.builder()
                        .title("작성된 문서가 없습니다.")
                        .build());
        Schedule schedule = scheduleRepository.findFirstByWorkSpaceIdOrderByCreatedAtDesc(workspace.getId())
                .orElse(Schedule.builder()
                        .content("작성된 일정이 없습니다.")
                        .build());
        return WorkspaceFindRecentData.of(document, schedule);
    }

    // 워크스페이스 정보 수정
    @Transactional
    public ResponseDto<WorkspaceResponseDto> updateWorkspace(Long workspaceId,
                                                             WorkSpaceUpdateReqeustDto requestDto,
                                                             UserDetails userDetails) throws IOException {
        // 1. 유저 가지고오기
        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        WorkSpace workspace = workspaceRepository.findById(workspaceId).orElse(null);
        if (workspace == null) {
            throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);
        }

        // 2. 유저와 관련된 workspaceId인지 확인할 것 => 아니라면, else Throw
        WorkSpaceUser workspaceUser = workspaceUserRepository.findByUserAndWorkSpaceId(user, workspaceId).orElse(null);
        if (workspaceUser == null) {
            throw new RequestException(ErrorCode.WORKSPACE_IN_USER_NOT_FOUND_404);
        }

        // 3. 데이터 수정하기
        String imageUrl = workspace.getImageUrl();
        if (requestDto.getImage() != null && !requestDto.getImage().equals("")) {
            try {
                String deleteUrl = imageUrl.substring(imageUrl.indexOf("workspace"));
                s3UploaderService.deleteFiles(deleteUrl);
            } catch (Exception e) {
                log.error("S3에 해당하는 이미지가 없습니다. ");
            }
            imageUrl = s3UploaderService.uploadBase64Image(requestDto.getImage(), "workspace");
        }

        workspace.update(requestDto, imageUrl);
        WorkspaceResponseDto responseDto = WorkspaceResponseDto.createResponseDto(workspace,null);

        return ResponseDto.success(responseDto);
    }

    //워크스페이스 내 회원 등록 (초대받은 멤버가 등록됨)
    @Transactional
    public ResponseDto<?> joinMemberInWorkspace(Long workspaceId, UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        WorkSpace workSpace = workspaceRepository.findById(workspaceId).orElse(null);
        if (workSpace == null) {
            throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);
        }

        Optional<WorkSpaceUser> byUserAndWorkSpaceId = workspaceUserRepository.findByUserAndWorkSpaceId(user, workspaceId);
        // 중복해서 들어오는 경우 예외처리
        if (byUserAndWorkSpaceId.isPresent()) {
            throw new RequestException(ErrorCode.WORKSPACE_DUPLICATION_409);
        }

        WorkSpaceUser workSpaceUser = WorkSpaceUser.of(user, workSpace);
        workspaceUserRepository.save(workSpaceUser);

        WorkspaceResponseDto responseDto = WorkspaceResponseDto.createResponseDto(workSpaceUser.getWorkSpace(),null);

        return ResponseDto.success(responseDto);
    }

    //워크스페이스 내 회원 조회
    public ResponseDto<List<UserResponseDto>> getMembersInWorkspace(Long workspaceId) {

        // 1. workspaceId에 해당하는 workspaceUser Entity를 꺼내기
        // 2. workspaceUser에 해당하는 User들을 모두 꺼내오기

        List<WorkSpaceUser> workSpaceUsers = workspaceUserRepository.findAllByWorkSpaceId(workspaceId);

        List<User> users = workSpaceUsers.stream().map(WorkSpaceUser::getUser).collect(Collectors.toList());


        List<UserResponseDto> userResponseDtos = users
                .stream()
                .map(user -> UserResponseDto.createResponseDto(
                        user,
                        workSpaceUsers.get(0).getWorkSpace().getCreatedBy().getUsername()))
                .collect(Collectors.toList());

        return ResponseDto.success(userResponseDtos);
    }

    //워크스페이스 나가기
    @Transactional
    public ResponseDto<Boolean> quitWorkspace(Long workspaceId, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        WorkSpace workspaceById = workspaceRepository.findById(workspaceId).orElse(null);
        if (workspaceById == null)
            throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);

        WorkSpaceUser workSpaceUser = workspaceUserRepository.findByUserAndWorkSpaceId(user, workspaceId).orElse(null);
        if (workSpaceUser == null) {
            throw new RequestException(ErrorCode.WORKSPACE_IN_USER_NOT_FOUND_404);
        }

        workspaceUserRepository.delete(workSpaceUser);
        return ResponseDto.success(true);
    }

    @Transactional
    public ResponseDto<Boolean> deleteWorkspace(Long workspaceId, UserDetails userDetails) {
        //
        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        WorkSpace workspaceById = workspaceRepository.findById(workspaceId).orElse(null);
        if (workspaceById == null)
            throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);

        WorkSpaceUser workSpaceUser = workspaceUserRepository.findByUserAndWorkSpaceId(user, workspaceId).orElse(null);
        if (workSpaceUser == null) {
            throw new RequestException(ErrorCode.WORKSPACE_IN_USER_NOT_FOUND_404);
        }

        try {
            String deleteUrl = workspaceById.getImageUrl().substring(workspaceById.getImageUrl().indexOf("workspace"));
            s3UploaderService.deleteFiles(deleteUrl);

        } catch (IndexOutOfBoundsException e) {
            log.info("기본 이미지");
        }

        workspaceRepository.delete(workspaceById);
        workspaceUserRepository.delete(workSpaceUser);

        return ResponseDto.success(true);
    }

    public ResponseDto<List<WorkspaceResponseDto>> getAllWorkspaces() {

        List<WorkSpace> allWorkspaces = workspaceRepository.findAll();
        List<WorkspaceResponseDto> responseDtos = allWorkspaces.stream().map(workSpace -> WorkspaceResponseDto.createResponseDto(workSpace,null)).collect(Collectors.toList());
        return ResponseDto.success(responseDtos);
    }

    public ResponseDto<MainResponseDto> getMain(Long workspaceId) {
        WorkSpace workSpace = workspaceRepository.findById(workspaceId).orElse(null);
        if (workSpace == null) {
            throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);
        }

        List<Document> documents = documentRepository.findAllByWorkSpaceIdOrderByCreatedAtDesc(workspaceId).stream().limit(4).collect(Collectors.toList());
        Notice firstNotice = noticeRepository.findFirstByWorkSpaceIdOrderByCreatedAtDesc(workspaceId).orElse(null);

        MainResponseDto responseDto = MainResponseDto.createResponseDto(workSpace, documents, firstNotice);
        return ResponseDto.success(responseDto);
    }

    public ResponseDto<WorkSpaceInfoResponseDto> getWorkspaceInfo(Long workspaceId) {

        WorkSpace workSpace = workspaceRepository.findById(workspaceId).orElse(null);

        if (workSpace == null) {
            throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);
        }

        int numInWorkspace = workspaceUserRepository.findAllByWorkSpaceId(workspaceId).size();

        return ResponseDto.success(WorkSpaceInfoResponseDto.of(workSpace, String.valueOf(numInWorkspace)));

    }


}