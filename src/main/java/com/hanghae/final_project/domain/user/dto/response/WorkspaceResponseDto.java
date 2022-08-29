package com.hanghae.final_project.domain.user.dto.response;

import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class WorkspaceResponseDto {

    private List<WorkSpace> workSpaces = new ArrayList<>();
}
