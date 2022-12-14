package com.hanghae.final_project.service.workspace;

import com.hanghae.final_project.api.workspace.dto.request.PagingRequestDto;
import com.hanghae.final_project.domain.repository.chat.ChatRoomRepository;
import com.hanghae.final_project.domain.model.*;
import com.hanghae.final_project.domain.repository.user.UserRepository;
import com.hanghae.final_project.domain.repository.workspace.*;
import com.hanghae.final_project.api.workspace.dto.request.WorkSpaceUpdateReqeustDto;
import com.hanghae.final_project.api.workspace.dto.request.WorkspaceFindRecentData;
import com.hanghae.final_project.api.workspace.dto.request.WorkspaceRequestDto;
import com.hanghae.final_project.api.workspace.dto.response.MainResponseDto;
import com.hanghae.final_project.api.workspace.dto.response.UserResponseDto;
import com.hanghae.final_project.api.workspace.dto.response.WorkSpaceInfoResponseDto;
import com.hanghae.final_project.api.workspace.dto.response.WorkspaceResponseDto;
import com.hanghae.final_project.infra.s3fileupload.S3UploaderService;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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

    public static final String WORKSPACE_STANDARD_IMG="https://hosunghan.s3.ap-northeast-2.amazonaws.com/workspace/workspaceimg.png";

    @Transactional
    public ResponseDto<WorkspaceResponseDto> createWorkspace(WorkspaceRequestDto requestDto,
                                                             UserDetails userDetails) throws IOException {

        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        // ?????? ??????????????? ????????????, ????????? ????????? ?????????

        // ???????????? ??????????????? ?????????, workspaceImage??? ???????????????. ?????? x
        // ???????????? ??????????????????, upload??? ?????????

        String imgUrl = WORKSPACE_STANDARD_IMG;
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

    // ????????? ?????? workspace ??????
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
                        .title("????????? ????????? ????????????.")
                        .build());
        Schedule schedule = scheduleRepository.findFirstByWorkSpaceIdOrderByCreatedAtDesc(workspace.getId())
                .orElse(Schedule.builder()
                        .content("????????? ????????? ????????????.")
                        .build());
        return WorkspaceFindRecentData.of(document, schedule);
    }

    // ?????????????????? ?????? ??????
    @Transactional
    public ResponseDto<WorkspaceResponseDto> updateWorkspace(Long workspaceId,
                                                             WorkSpaceUpdateReqeustDto requestDto,
                                                             UserDetails userDetails) throws IOException {
        // 1. ?????? ???????????????
        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        WorkSpace workspace = workspaceRepository.findById(workspaceId).orElse(null);
        if (workspace == null) {
            throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);
        }

        // 2. ????????? ????????? workspaceId?????? ????????? ??? => ????????????, else Throw
        WorkSpaceUser workspaceUser = workspaceUserRepository.findByUserAndWorkSpaceId(user, workspaceId).orElse(null);
        if (workspaceUser == null) {
            throw new RequestException(ErrorCode.WORKSPACE_IN_USER_NOT_FOUND_404);
        }

        // 3. ????????? ????????????
        String imageUrl = workspace.getImageUrl();
        if (requestDto.getImage() != null && !requestDto.getImage().equals("")) {
            try {
                //String deleteUrl = imageUrl.substring(imageUrl.indexOf("workspace"));
                s3UploaderService.deleteFiles(imageUrl,"workspace");
            } catch (Exception e) {
                log.error("S3??? ???????????? ???????????? ????????????. ");
            }
            imageUrl = s3UploaderService.uploadBase64Image(requestDto.getImage(), "workspace");
        }

        workspace.update(requestDto, imageUrl);
        WorkspaceResponseDto responseDto = WorkspaceResponseDto.createResponseDto(workspace,null);

        return ResponseDto.success(responseDto);
    }

    //?????????????????? ??? ?????? ?????? (???????????? ????????? ?????????)
    @Transactional
    public ResponseDto<?> joinMemberInWorkspace(Long workspaceId, UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        WorkSpace workSpace = workspaceRepository.findById(workspaceId).orElse(null);
        if (workSpace == null) {
            throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);
        }

        Optional<WorkSpaceUser> byUserAndWorkSpaceId = workspaceUserRepository.findByUserAndWorkSpaceId(user, workspaceId);
        // ???????????? ???????????? ?????? ????????????
        if (byUserAndWorkSpaceId.isPresent()) {
            throw new RequestException(ErrorCode.WORKSPACE_DUPLICATION_409);
        }

        WorkSpaceUser workSpaceUser = WorkSpaceUser.of(user, workSpace);
        workspaceUserRepository.save(workSpaceUser);

        WorkspaceResponseDto responseDto = WorkspaceResponseDto.createResponseDto(workSpaceUser.getWorkSpace(),null);

        return ResponseDto.success(responseDto);
    }

    //?????????????????? ??? ?????? ??????
    public ResponseDto<List<UserResponseDto>> getMembersInWorkspace(Long workspaceId, PagingRequestDto requestDto) {

        // 1. workspaceId??? ???????????? workspaceUser Entity??? ?????????
        // 2. workspaceUser??? ???????????? User?????? ?????? ????????????

        Long cursor;
        WorkSpace workSpace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()->new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404));

        if(requestDto==null){
            cursor=workspaceUserRepository.findByUserAndWorkSpaceId(workSpace.getCreatedBy(),workspaceId)
                    .orElseThrow(()->new RequestException(ErrorCode.WORKSPACE_IN_USER_NOT_FOUND_404)).getId()-1L;
        }
        else cursor = workspaceUserRepository.findByUserIdAndWorkSpaceId(requestDto.getCursorId(), workspaceId)
                .orElseThrow(()->new RequestException(ErrorCode.WORKSPACE_IN_USER_NOT_FOUND_404)).getId();

        Slice<WorkSpaceUser> workSpaceUsers = workspaceUserRepository
                .findAllByIdAfterAndWorkSpace_IdOrderByIdAsc(cursor,workspaceId, PageRequest.of(0,10));

        List<User> users = workSpaceUsers.stream().map(WorkSpaceUser::getUser).collect(Collectors.toList());


        List<UserResponseDto> userResponseDtos = users
                .stream()
                .map(user -> UserResponseDto.createResponseDto(
                        user,
                        workSpace.getCreatedBy().getUsername()))
                .collect(Collectors.toList());

        return ResponseDto.success(userResponseDtos);
    }

    //?????????????????? ?????????
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
            //String deleteUrl = workspaceById.getImageUrl().substring(workspaceById.getImageUrl().indexOf("workspace"));
            s3UploaderService.deleteFiles(workspaceById.getImageUrl(),"workspace");

        } catch (IndexOutOfBoundsException e) {
            log.info("?????? ?????????");
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
        Notice firstNotice = noticeRepository.findFirstByWorkSpaceIdOrderByCreatedAtAsc(workspaceId).orElse(null);

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