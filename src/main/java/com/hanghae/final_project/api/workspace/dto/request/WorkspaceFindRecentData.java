package com.hanghae.final_project.api.workspace.dto.request;

import com.hanghae.final_project.domain.model.Document;
import com.hanghae.final_project.domain.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkspaceFindRecentData {

    private String documentTitle;
    private String scheduleContent;
    private String documentCreatedAt;
    private String scheduleCreatedAt;

    public static WorkspaceFindRecentData of(Document document, Schedule schedule){
        return WorkspaceFindRecentData.builder()
                .documentCreatedAt(document.getCreatedAt())
                .documentTitle(document.getTitle())
                .scheduleCreatedAt(schedule.getCreatedAt())
                .scheduleContent(schedule.getContent())
                .build();
    }
}
