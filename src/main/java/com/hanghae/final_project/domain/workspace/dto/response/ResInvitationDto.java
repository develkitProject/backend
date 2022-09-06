package com.hanghae.final_project.domain.workspace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResInvitationDto {
    private Long workspaceId;
    private String code;

    public static ResInvitationDto of(Long workspaceId,String code){

        return ResInvitationDto.builder()
                .workspaceId(workspaceId)
                .code(code)
                .build();
    }
}
