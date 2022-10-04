package com.hanghae.final_project.api.workspace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DocumentRequestDto {

    private String title;
    private String content;
    private List<String> preFileUrls;

}

