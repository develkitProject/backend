package com.hanghae.final_project.api.workspace.dto.response;

import com.hanghae.final_project.domain.model.Document;
import com.hanghae.final_project.domain.model.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class DocumentListResponseDto extends Timestamped {
    private Long id;
    private String title;
//    private String imageUrl;
    private String nickname;
    private Long workSpaceId;
    private String createdAt;
    private String modifiedAt;

    public DocumentListResponseDto(Document document) {
        this.title = document.getTitle();
        this.nickname = document.getUser().getNickname();
        this.workSpaceId = document.getWorkSpace().getId();
    }
}
