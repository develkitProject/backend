package com.hanghae.final_project.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceRequestDto {

    private String title;
    private String content;

    // test용
    public WorkspaceRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
