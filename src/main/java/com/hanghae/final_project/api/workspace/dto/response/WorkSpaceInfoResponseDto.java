package com.hanghae.final_project.api.workspace.dto.response;

import com.hanghae.final_project.domain.model.WorkSpace;
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
    private String imageUrl;
    private String createUserEmail;


    public static WorkSpaceInfoResponseDto of(WorkSpace workSpace, String userNumInWorkSpace){

        return WorkSpaceInfoResponseDto.builder()
                .createUserEmail(workSpace.getCreatedBy().getUsername())
                .title(workSpace.getTitle())
                .imageUrl(workSpace.getImageUrl())
                .createdAt(workSpace.getCreatedAt())
                .content(workSpace.getContent())
                .userNumInWorkSpace(userNumInWorkSpace)
                .build();
    }
}
