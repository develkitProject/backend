package com.hanghae.final_project.api.workspace.dto.request;


import com.hanghae.final_project.domain.model.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeRequestDto {

    private String title;
    private String content;

    public Notice toEntity() {
        return Notice.builder()
                .title(title)
                .content(content)
                .build();
    }
}
