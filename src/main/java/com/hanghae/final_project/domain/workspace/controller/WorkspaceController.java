package com.hanghae.final_project.domain.workspace.controller;

import com.hanghae.final_project.domain.workspace.dto.request.WorkspaceJoinRequestDto;
import com.hanghae.final_project.domain.workspace.dto.request.WorkspaceRequestDto;
import com.hanghae.final_project.domain.workspace.dto.response.MainResponseDto;
import com.hanghae.final_project.domain.workspace.dto.response.UserResponseDto;
import com.hanghae.final_project.domain.workspace.dto.response.WorkspaceResponseDto;
import com.hanghae.final_project.domain.workspace.service.WorkspaceService;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    // 워크 스페이스 생성
    @PostMapping
    public ResponseDto<WorkspaceResponseDto> createWorkspace(@Valid @RequestBody WorkspaceRequestDto requestDto,
                                                             Errors errors,
                                                             @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        log.info("요청 메소드 [POST] /api/workspaces");
        if (errors.hasErrors()) {
            log.info("error : {}", errors.getAllErrors().get(0).getDefaultMessage());
            throw new RequestException(ErrorCode.WORKSPACE_INFO_NOT_FORMATTED, errors.getAllErrors().get(0).getDefaultMessage());
        }
        return workspaceService.createWorkspace(requestDto, userDetails);
    }

    // 참여한 모든 workspace 조회
    @GetMapping
    public ResponseDto<List<WorkspaceResponseDto>> getWorkspaces(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("요청 메소드 [GET] /api/workspaces");
        return workspaceService.getWorkspaces(userDetails);
    }

    // 워크스페이스 정보 수정
    @PutMapping("/{workspaceId}")
    public ResponseDto<WorkspaceResponseDto> updateWorkspace(@PathVariable Long workspaceId,
                                          @Valid @RequestBody WorkspaceRequestDto requestDto,
                                          Errors errors,
                                          @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        log.info("요청 메소드 [PUT] /api/workspaces/" + workspaceId);
        if (errors.hasErrors()) {
            log.info("error : {}", errors.getAllErrors().get(0).getDefaultMessage());
            throw new RequestException(ErrorCode.WORKSPACE_INFO_NOT_FORMATTED, errors.getAllErrors().get(0).getDefaultMessage());
        }
        return workspaceService.updateWorkspace(workspaceId, requestDto, userDetails);
    }

    /*// 워크스페이스 나가기
    @DeleteMapping("/{workspaceId}")
    public ResponseDto<?> deleteWorkspace(@PathVariable Long workspaceId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        return workspaceService.deleteWorkspace(workspaceId, userDetails);
    }*/

    //워크스페이스 내 회원 등록 (초대받은 멤버가 등록됨)
    @PostMapping("/join/{workspaceId}")
    public ResponseDto<?> joinMemberInWorkspace(@PathVariable Long workspaceId,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        log.info("요청 메소드 [POST] /api/workspaces/join/" + workspaceId);
        return workspaceService.joinMemberInWorkspace(workspaceId, userDetails);
    }

    //워크스페이스 내 회원 조회
    @GetMapping("/{workspaceId}")
    public ResponseDto<List<UserResponseDto>> getMembersInWorkspace(@PathVariable Long workspaceId) {
        log.info("요청 메소드 [GET] /api/workspaces/" + workspaceId);
        return workspaceService.getMembersInWorkspace(workspaceId);
    }

    //워크스페이스 나가기
    @DeleteMapping("/quit/{workspaceId}")
    public ResponseDto<Boolean> quitWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("요청 메소드 [DELETE] /api/workspaces/" + workspaceId);
        return workspaceService.quitWorkspace(workspaceId, userDetails);
    }

    // 초대코드가 동일한지 확인하는 controller
    @DeleteMapping("/{workspaceId}")
    public ResponseDto<Boolean> deleteWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal UserDetails userDetails) {
        return workspaceService.deleteWorkspace(workspaceId, userDetails);
    }

    //모든 workspace 조회
    @GetMapping("/temp")
    public ResponseDto<List<WorkspaceResponseDto>> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    // workspace의 공지사항과 게시글 목록 반환
    @GetMapping("/{workspaceId}/main")
    public ResponseDto<MainResponseDto> getMain(@PathVariable Long workspaceId) {
        return workspaceService.getMain(workspaceId);
    }


}
