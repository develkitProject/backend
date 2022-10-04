package com.hanghae.final_project.api.workspace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceRequestDto {

    @NotNull(message = "제목은 필수 입력 값입니다")
    private String title;

    @NotNull(message = "소개는 필수 입력 값입니다")
    private String content;

    private String image;
}