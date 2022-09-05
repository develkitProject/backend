package com.hanghae.final_project.domain.workspace.dto;


import com.hanghae.final_project.domain.workspace.model.Notice;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NoticeResponseDto {

    private Long id;
    private String title;
    private String content;
    private Long workspaceId;
    private String nickname;

    public static NoticeResponseDto of(Notice notice) {
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .workspaceId(notice.getWorkSpace().getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .build();
    }
}





