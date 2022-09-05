package com.hanghae.final_project.domain.workspace.dto.response;

import com.hanghae.final_project.domain.workspace.model.Document;
import com.hanghae.final_project.domain.workspace.model.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class DocumentListResponseDto extends Timestamped {
    private String title;
    private String nickname;
    private Long workSpaceId;

    public DocumentListResponseDto(Document document) {
        this.title = document.getTitle();
        this.nickname = document.getUser().getNickname();
        this.workSpaceId = document.getWorkSpace().getId();
    }
}
