package com.hanghae.final_project.domain.workspace.controller;

import com.hanghae.final_project.domain.workspace.dto.request.WorkspaceJoinRequestDto;
import com.hanghae.final_project.domain.workspace.dto.request.WorkspaceRequestDto;
import com.hanghae.final_project.domain.workspace.service.WorkspaceService;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    // 워크 스페이스 생성
    @PostMapping
    public ResponseDto<?> createWorkspace(@Valid @RequestBody WorkspaceRequestDto requestDto,
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
    public ResponseDto<?> getWorkspaces(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("요청 메소드 [GET] /api/workspaces");
        return workspaceService.getWorkspaces(userDetails);
    }

    // 워크스페이스 정보 수정
    @PutMapping("/{workspaceId}")
    public ResponseDto<?> updateWorkspace(@PathVariable Long workspaceId,
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
                                                @Valid @RequestBody WorkspaceJoinRequestDto requestDto,
                                                Errors errors,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        if (errors.hasErrors()) {
            throw new RequestException(ErrorCode.WORKSPACE_INVITATION_CODE_DOES_NOT_EXIST, errors.getAllErrors().get(0).getDefaultMessage());
        }
        log.info("요청 메소드 [POST] /api/workspaces/join/" + workspaceId);
        return workspaceService.joinMemberInWorkspace(workspaceId, requestDto, userDetails);
    }

    //워크스페이스 내 회원 조회
    @GetMapping("/{workspaceId}")
    public ResponseDto<?> getMembersInWorkspace(@PathVariable Long workspaceId) {
        log.info("요청 메소드 [GET] /api/workspaces/" + workspaceId);
        return workspaceService.getMembersInWorkspace(workspaceId);
    }

    //워크스페이스 나가기
    @DeleteMapping("/quit/{workspaceId}")
    public ResponseDto<?> quitWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("요청 메소드 [DELETE] /api/workspaces/" + workspaceId);
        return workspaceService.quitWorkspace(workspaceId, userDetails);
    }

    // 초대코드가 동일한지 확인하는 controller
    @DeleteMapping("/{workspaceId}")
    public ResponseDto<?> deleteWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal UserDetails userDetails) {
        return workspaceService.deleteWorkspace(workspaceId, userDetails);
    }

    //모든 workspace 조회
    @GetMapping("/temp")
    public ResponseDto<?> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    // workspace의 공지사항과 게시글 목록 반환
    @GetMapping("/{workspaceId}/main")
    public ResponseDto<?> getMain(@PathVariable Long workspaceId) {
        return workspaceService.getMain(workspaceId);
    }

}
