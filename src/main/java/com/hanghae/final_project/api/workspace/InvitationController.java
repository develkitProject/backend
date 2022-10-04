package com.hanghae.final_project.api.workspace;

import com.hanghae.final_project.api.workspace.dto.request.InviteRequestDto;
import com.hanghae.final_project.api.workspace.dto.response.InvitationResponseDto;
import com.hanghae.final_project.api.workspace.dto.response.WorkspaceResponseDto;
import com.hanghae.final_project.service.workspace.InvitationService;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "WorkSpace Invitation Code")
@RequiredArgsConstructor
@RestController
public class InvitationController {

    private final InvitationService invitationService;

    @ApiOperation(value = "워크스페이스 초대 코드 조회", notes = "워크스페이스에 따라 구분")
    @GetMapping("/api/invitation/{workspaceId}")
    public ResponseEntity<ResponseDto<InvitationResponseDto>> getInvitationCode(@PathVariable Long workspaceId,
                                                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return invitationService.getCode(workspaceId,userDetails);
    }

    @ApiOperation(value = "워크스페이스 초대 코드 입력", notes = "워크스페이스에 따라 구분")
    @PostMapping("/api/invitation/codes")
    public ResponseDto<WorkspaceResponseDto> getWorkSpaceByCode(@RequestBody InviteRequestDto requestDto){
       return invitationService.getWorkSpaceByCode(requestDto);
    }
}