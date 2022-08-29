package com.hanghae.final_project.domain.user.service;

import com.hanghae.final_project.domain.user.dto.request.WorkspaceRequestDto;
import com.hanghae.final_project.domain.user.repository.WorkspaceUserRepository;
import com.hanghae.final_project.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceUserRepository workspaceUserRepository;


    public ResponseDto<?> createWorkspace(WorkspaceRequestDto requestDto, UserDetails userDetails) {
        return ResponseDto.success(true);
    }

    public ResponseDto<?> getWorkspaces(UserDetails userDetails) {

        return ResponseDto.success(true);
    }

    public ResponseDto<?> updateWorkspace(Long workspaceId, WorkspaceRequestDto requestDto, UserDetails userDetails) {
        return ResponseDto.success(true);
    }

    public ResponseDto<?> deleteWorkspace(Long workspaceId, UserDetails userDetails) {
        return ResponseDto.success(true);
    }

    public ResponseDto<?> joinMemberInWorkspace(UserDetails userDetails) {
        return ResponseDto.success(true);
    }

    public ResponseDto<?> getMembersInWorkspace(UserDetails userDetails) {
        return ResponseDto.success(true);
    }

    public ResponseDto<?> quitWorkspace(UserDetails userDetails) {
        return ResponseDto.success(true);
    }
}
