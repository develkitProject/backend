package com.hanghae.final_project.domain.user.service;

import com.hanghae.final_project.domain.user.dto.request.WorkspaceRequestDto;
import com.hanghae.final_project.domain.user.dto.response.WorkspaceResponseDto;
import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.user.repository.UserRepository;
import com.hanghae.final_project.domain.user.repository.WorkspaceRepository;
import com.hanghae.final_project.domain.user.repository.WorkspaceUserRepository;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.model.WorkSpaceUser;
import com.hanghae.final_project.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {
    private final WorkspaceUserRepository workspaceUserRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public ResponseDto<?> createWorkspace(WorkspaceRequestDto requestDto, UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        // 워크 스페이스를 생성하고, 자신의 정보를 넣어줌
        WorkSpace workSpace = WorkSpace.of(requestDto);

        WorkSpaceUser workSpaceUser = WorkSpaceUser.of(user, workSpace);
        return ResponseDto.success(workSpaceUser);
    }

    // 참여한 모든 workspace 조회
    public ResponseDto<?> getWorkspaces(UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        List<WorkSpaceUser> allByUser = workspaceUserRepository.findAllByUser(user);


        return ResponseDto.success();
    }

    // 워크스페이스 정보 수정
    public ResponseDto<?> updateWorkspace(Long workspaceId, WorkspaceRequestDto requestDto, UserDetails userDetails) {
        return ResponseDto.success(true);
    }

    //워크스페이스 내 회원 등록 (초대받은 멤버가 등록됨)
    public ResponseDto<?> joinMemberInWorkspace(UserDetails userDetails) {
        return ResponseDto.success(true);
    }

    //워크스페이스 내 회원 조회
    public ResponseDto<?> getMembersInWorkspace(UserDetails userDetails) {
        return ResponseDto.success(true);
    }

    //워크스페이스 나가기
    public ResponseDto<?> quitWorkspace(UserDetails userDetails) {
        return ResponseDto.success(true);
    }

}
