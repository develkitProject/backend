package com.hanghae.final_project.domain.workspace.service;

import com.hanghae.final_project.domain.workspace.dto.request.WorkspaceRequestDto;
import com.hanghae.final_project.domain.workspace.dto.response.WorkspaceResponseDto;
import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.user.repository.UserRepository;
import com.hanghae.final_project.domain.workspace.repository.WorkspaceRepository;
import com.hanghae.final_project.domain.workspace.repository.WorkspaceUserRepository;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {
    private final WorkspaceUserRepository workspaceUserRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseDto<?> createWorkspace(WorkspaceRequestDto requestDto, UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        // 워크 스페이스를 생성하고, 자신의 정보를 넣어줌
        WorkSpace workSpace = WorkSpace.of(requestDto);

        WorkSpaceUser workSpaceUser = WorkSpaceUser.of(user, workSpace);
        WorkSpaceUser save = workspaceUserRepository.save(workSpaceUser);

        return ResponseDto.success(save);
    }

    // 참여한 모든 workspace 조회
    public ResponseDto<?> getWorkspaces(UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        List<WorkSpaceUser> allWorkSpaceUserByUser = workspaceUserRepository.findAllByUser(user);

        WorkspaceResponseDto responseDto = new WorkspaceResponseDto(allWorkSpaceUserByUser.stream().map(list -> list.getWorkSpace()).collect(Collectors.toList()));

        return ResponseDto.success(responseDto);
    }

    // 워크스페이스 정보 수정
    @Transactional
    public ResponseDto<?> updateWorkspace(Long workspaceId, WorkspaceRequestDto requestDto, UserDetails userDetails) {
        // 1. 유저 가지고오기
        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        // 2. 유저와 관련된 workspaceId인지 확인할 것 => 아니라면, else Throw
        WorkSpaceUser workspaceUser = workspaceUserRepository.findByUserAndWorkSpaceId(user, workspaceId).orElse(null);
        if (workspaceUser == null) {
            throw new IllegalArgumentException("workspace에 존재하는 데이터가 아닙니다");
        }

        // 3. 데이터 수정하기
        workspaceUser.getWorkSpace().update(requestDto);

        return ResponseDto.success(true);
    }

    //워크스페이스 내 회원 등록 (초대받은 멤버가 등록됨)
    @Transactional
    public ResponseDto<?> joinMemberInWorkspace(Long workspaceId, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        WorkSpace workSpace = workspaceRepository.findById(workspaceId).get();

        WorkSpaceUser workSpaceUser = WorkSpaceUser.of(user, workSpace);
        workspaceUserRepository.save(workSpaceUser);

        return ResponseDto.success(true);
    }

    //워크스페이스 내 회원 조회
    public ResponseDto<?> getMembersInWorkspace(Long workspaceId) {
        // 1. workspaceId에 해당하는 workspaceUser Entity를 꺼내기
        // 2. workspaceUser에 해당하는 User들을 모두 꺼내오기

        List<WorkSpaceUser> workSpaceUsers = workspaceUserRepository.findAllByWorkSpaceId(workspaceId);
        WorkspaceResponseDto responseDto = new WorkspaceResponseDto(workSpaceUsers.stream().map(list -> list.getUser()).collect(Collectors.toList()));

        return ResponseDto.success(responseDto);
    }

    //워크스페이스 나가기
    @Transactional
    public ResponseDto<?> quitWorkspace(Long workspaceId, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        WorkSpaceUser workSpaceUser = workspaceUserRepository.findByUserAndWorkSpaceId(user, workspaceId).orElse(null);
        if (workSpaceUser == null) {
            throw new IllegalArgumentException("workspace에 존재하는 데이터가 아닙니다");
        }

        workspaceUserRepository.delete(workSpaceUser);
        return ResponseDto.success(true);
    }
}
