package com.hanghae.final_project.api.workspace.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class WorkspaceJoinRequestDto {

    @NotNull(message = "초대 코드를 입력해주세요")
    private String code;
}
