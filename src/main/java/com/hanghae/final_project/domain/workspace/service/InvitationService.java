package com.hanghae.final_project.domain.workspace.service;

import com.hanghae.final_project.domain.workspace.dto.response.ResInvitationDto;
import com.hanghae.final_project.domain.workspace.model.Invitation;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.model.WorkSpaceUser;
import com.hanghae.final_project.domain.workspace.repository.InvitationRepository;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import com.hanghae.final_project.domain.workspace.repository.WorkspaceUserRepository;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import com.hanghae.final_project.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class InvitationService {

    private final InvitationRepository invitationRepository;

    private final WorkspaceUserRepository workspaceUserRepository;

    private final WorkSpaceRepository workSpaceRepository;

    public ResponseEntity<?> getCode(Long workspaceId, UserDetailsImpl userDetails) {

        //Workspace 가 존재하는지 확인
        WorkSpace workSpace=  checkWorkspace(workspaceId);

        //해당 워크스페이스에 존재하는 유저인지 확인
        WorkSpaceUser workSpaceUser = validateUser(workspaceId, userDetails);

        //code 만들기
        String invitationCode = makeCode(workspaceId,workSpace);

        //dto 생성
        ResInvitationDto resInvitationDto =ResInvitationDto.of(
                workSpaceUser.getWorkSpace().getId(),
                invitationCode
        );

        return new ResponseEntity<>(ResponseDto.success(resInvitationDto), HttpStatus.OK);
    }

    //해당 workspace 에 포함된 유저가 요청을 보낸것인지 확인하는 과정
    private WorkSpaceUser validateUser(Long workspaceId, UserDetailsImpl userDetails) {
       return workspaceUserRepository.findByUserAndWorkSpaceId(userDetails.getUser(), workspaceId)
                .orElseThrow(() -> new RequestException(ErrorCode.WORKSPACE_IN_USER_NOT_FOUND_404));
    }

    //코드 만들기
    //코드 Repository에 기록하기
    private String makeCode(Long workspaceId,WorkSpace workSpace){

        String invitationCode;

        //workspaceId를 통해서 초대코드가 존재하는지 확인
        Invitation invitation
                = invitationRepository.findByWorkSpace_Id(workspaceId).orElse(null);

        //workspace의 초대코드가 존재하지 않을 경우
        //새로 코드 생성후 DB저장
        if(invitation==null){
            invitationCode = UUID.randomUUID().toString();
            invitation = Invitation.of(invitationCode,workSpace);
            invitationRepository.save(invitation);
            return invitationCode;
        }

        // 기존의 존재하다면 초대코드값 리턴
        return invitation.getInvite();

    }

    private WorkSpace checkWorkspace(Long workspaceId){
        return workSpaceRepository.findById(workspaceId)
                .orElseThrow(()->new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404));
    }

}