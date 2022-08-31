package com.hanghae.final_project.domain.workspace.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DocumentRequestDto {

    private String title;
    private String content;
    private String imageUrl;

}

