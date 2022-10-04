package com.hanghae.final_project.api.workspace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter

@NoArgsConstructor
@AllArgsConstructor
public class WorkSpaceUpdateReqeustDto {

    private String title;

    private String content;

    private String image;
}
