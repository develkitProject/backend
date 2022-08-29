package com.hanghae.final_project.domain.user.controller;

import com.hanghae.final_project.domain.user.dto.request.WorkspaceRequestDto;
import com.hanghae.final_project.domain.user.service.WorkspaceService;
import com.hanghae.final_project.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    // 워크 스페이스 생성
    @PostMapping()
    public ResponseDto<?> createWorkspace(@RequestBody WorkspaceRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        return workspaceService.createWorkspace(requestDto, userDetails);
    }

    // 참여한 모든 workspace 조회
    @GetMapping()
    public ResponseDto<?> getWorkspaces(@AuthenticationPrincipal UserDetails userDetails) {
        return workspaceService.getWorkspaces(userDetails);
    }

    // 워크스페이스 정보 수정
    @PutMapping("/{workspaceId}")
    public ResponseDto<?> updateWorkspace(@PathVariable Long workspaceId,
                                          @RequestBody WorkspaceRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        return workspaceService.updateWorkspace(workspaceId, requestDto, userDetails);
    }

    /*// 워크스페이스 나가기
    @DeleteMapping("/{workspaceId}")
    public ResponseDto<?> deleteWorkspace(@PathVariable Long workspaceId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        return workspaceService.deleteWorkspace(workspaceId, userDetails);
    }*/

    //워크스페이스 내 회원 등록 (초대받은 멤버가 등록됨)
    @PostMapping("/join")
    public ResponseDto<?> joinMemberInWorkspace(@AuthenticationPrincipal UserDetails userDetails) {
        return workspaceService.joinMemberInWorkspace(userDetails);
    }

    //워크스페이스 내 회원 조회
    @GetMapping("/{workspaceId}")
    public ResponseDto<?> getMembersInWorkspace(@AuthenticationPrincipal UserDetails userDetails) {
        return workspaceService.getMembersInWorkspace(userDetails);
    }

    //워크스페이스 나가기
    @DeleteMapping("/{workspaceId}")
    public ResponseDto<?> quitWorkspace(@AuthenticationPrincipal UserDetails userDetails) {
        return workspaceService.quitWorkspace(userDetails);
    }
}
