package com.hanghae.final_project.api.workspace;

import com.hanghae.final_project.api.workspace.dto.request.PagingRequestDto;
import com.hanghae.final_project.api.workspace.dto.request.WorkSpaceUpdateReqeustDto;
import com.hanghae.final_project.api.workspace.dto.request.WorkspaceRequestDto;
import com.hanghae.final_project.api.workspace.dto.response.MainResponseDto;
import com.hanghae.final_project.api.workspace.dto.response.UserResponseDto;
import com.hanghae.final_project.api.workspace.dto.response.WorkSpaceInfoResponseDto;
import com.hanghae.final_project.api.workspace.dto.response.WorkspaceResponseDto;
import com.hanghae.final_project.global.util.annotation.QueryStringArgResolver;
import com.hanghae.final_project.service.workspace.WorkspaceService;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Api(tags = "WorkSpace")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @ApiOperation(value = "워크스페이스 생성", notes = "워크스페이스에 따라 구분")
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

    @ApiOperation(value = "참여한 모든 WorkSpcae 조회", notes = "사용자에 따라 구분")
    @GetMapping
    public ResponseDto<List<WorkspaceResponseDto>> getWorkspaces(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("요청 메소드 [GET] /api/workspaces");
        return workspaceService.getWorkspaces(userDetails);
    }

    @ApiOperation(value = "워크스페이스 정보 수정", notes = "워크스페이스에 따라 구분")
    @PutMapping("/{workspaceId}")
    public ResponseDto<WorkspaceResponseDto> updateWorkspace(@PathVariable Long workspaceId,
                                                             @RequestBody WorkSpaceUpdateReqeustDto requestDto,
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

    @ApiOperation(value = "워크스페이스 내 회원 등록(초대받은 멤버가 등록됨)", notes = "워크스페이스에 따라 구분")
    @PostMapping("/join/{workspaceId}")
    public ResponseDto<?> joinMemberInWorkspace(@PathVariable Long workspaceId,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        log.info("요청 메소드 [POST] /api/workspaces/join/" + workspaceId);
        return workspaceService.joinMemberInWorkspace(workspaceId, userDetails);
    }

    @ApiOperation(value = "워크스페이스 내 회원 조회", notes = "워크스페이스에 따라 구분")
    @GetMapping("/{workspaceId}")
    public ResponseDto<List<UserResponseDto>> getMembersInWorkspace(@PathVariable Long workspaceId,
                                                                    @QueryStringArgResolver PagingRequestDto requestDto) {
        log.info("요청 메소드 [GET] /api/workspaces/" + workspaceId);
        return workspaceService.getMembersInWorkspace(workspaceId,requestDto);
    }

    @ApiOperation(value = "워크스페이스 나가기", notes = "워크스페이스에 따라 구분")
    @DeleteMapping("/quit/{workspaceId}")
    public ResponseDto<Boolean> quitWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("요청 메소드 [DELETE] /api/workspaces/" + workspaceId);
        return workspaceService.quitWorkspace(workspaceId, userDetails);
    }

    @ApiOperation(value = "워크스페이스 삭제", notes = "워크스페이스에 따라 구분")
    @DeleteMapping("/{workspaceId}")
    public ResponseDto<Boolean> deleteWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal UserDetails userDetails) {
        return workspaceService.deleteWorkspace(workspaceId, userDetails);
    }

    @ApiOperation(value = "모든 워크스페이스 조회", notes = "")
    @GetMapping("/temp")
    public ResponseDto<List<WorkspaceResponseDto>> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    @ApiOperation(value = "워크스페이스의 공지사항과 게시글 목록 반환", notes = "워크스페이스에 따라 구분")
    @GetMapping("/{workspaceId}/main")
    public ResponseDto<MainResponseDto> getMain(@PathVariable Long workspaceId) {
        return workspaceService.getMain(workspaceId);
    }

    @ApiOperation(value = "워크스페이스 정보 및 인원수 반환", notes = "")
    @GetMapping("/{workspaceId}/info")
    public ResponseDto<WorkSpaceInfoResponseDto> getWorkspaceInfo(@PathVariable Long workspaceId) {
        return workspaceService.getWorkspaceInfo(workspaceId);
    }


}
