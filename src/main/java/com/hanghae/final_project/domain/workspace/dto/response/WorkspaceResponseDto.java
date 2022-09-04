package com.hanghae.final_project.domain.workspace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class WorkspaceResponseDto<T> {

    private List<T> workSpaces = new ArrayList<>();
}
