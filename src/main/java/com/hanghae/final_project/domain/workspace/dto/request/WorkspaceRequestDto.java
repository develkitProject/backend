package com.hanghae.final_project.domain.workspace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class WorkspaceRequestDto {
    private String title;
    private String content;
}
