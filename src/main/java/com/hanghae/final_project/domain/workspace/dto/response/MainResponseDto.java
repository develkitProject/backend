package com.hanghae.final_project.domain.workspace.dto.response;

import com.hanghae.final_project.domain.workspace.model.Document;
import com.hanghae.final_project.domain.workspace.model.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class MainResponseDto {
    private List<Document> documents;
    private Notice notice;

    public static MainResponseDto createResponseDto(List<Document> documents, Notice notice) {
        return MainResponseDto.builder()
                .documents(documents)
                .notice(notice)
                .build();
    }
}
