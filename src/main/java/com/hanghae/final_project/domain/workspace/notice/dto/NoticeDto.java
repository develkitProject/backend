package com.hanghae.final_project.domain.workspace.notice.dto;

import com.hanghae.final_project.domain.workspace.model.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class NoticeDto {

    private Long id;
    private String title;
    private String content;
    private String createdAt;

}
