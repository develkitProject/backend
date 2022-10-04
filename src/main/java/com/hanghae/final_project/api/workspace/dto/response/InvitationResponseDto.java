package com.hanghae.final_project.api.workspace.dto.response;

import com.hanghae.final_project.domain.model.WorkSpace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InvitationResponseDto {
    private Long workspaceId;
    private String code;
    private String workspaceTitle;
    public static InvitationResponseDto of(WorkSpace workSpace, String code){

        return InvitationResponseDto.builder()
                .workspaceId(workSpace.getId())
                .workspaceTitle(workSpace.getTitle())
                .code(code)
                .build();
    }
}
