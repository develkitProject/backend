package com.hanghae.final_project.domain.workspace.controller;

import com.hanghae.final_project.domain.workspace.dto.request.InviteRequestDto;
import com.hanghae.final_project.domain.workspace.dto.response.ResInvitationDto;
import com.hanghae.final_project.domain.workspace.dto.response.WorkspaceResponseDto;
import com.hanghae.final_project.domain.workspace.service.InvitationService;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class InvitationController {

    private final InvitationService invitationService;

    @GetMapping("/api/invitation/{workspaceId}")
    public ResponseEntity<ResponseDto<ResInvitationDto>> getInvitationCode(@PathVariable Long workspaceId,
                                                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        return invitationService.getCode(workspaceId,userDetails);
    }

    @PostMapping("/api/invitation/codes")
    public ResponseEntity<ResponseDto<WorkspaceResponseDto>> getWorkSpaceByCode(@RequestBody InviteRequestDto requestDto){
       return invitationService.getWorkSpaceByCode(requestDto);
    }
}
