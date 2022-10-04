package com.hanghae.final_project.api.workspace.dto.response;

import com.hanghae.final_project.domain.model.Schedule;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResSchedulesDto {

    private Long id;
    private String content;
    private String eventDate;
    private Long workspaceId;

    public static ResSchedulesDto of(Schedule schedule) {
        return ResSchedulesDto.builder()
                .id(schedule.getId())
                .workspaceId(schedule.getWorkSpace().getId())
                .content(schedule.getContent())
                .eventDate(schedule.getDate())
                .build();
    }
}
