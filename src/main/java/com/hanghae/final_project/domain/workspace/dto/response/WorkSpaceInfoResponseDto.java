package com.hanghae.final_project.domain.workspace.dto.response;

import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WorkSpaceInfoResponseDto {

    private String title;
    private String content;
    private String createdAt;
    private String userNumInWorkSpace;


    public static WorkSpaceInfoResponseDto of(WorkSpace workSpace, String userNumInWorkSpace){

        return WorkSpaceInfoResponseDto.builder()
                .title(workSpace.getTitle())
                .createdAt(workSpace.getCreatedAt())
                .content(workSpace.getContent())
                .userNumInWorkSpace(userNumInWorkSpace)
                .build();
    }
}
