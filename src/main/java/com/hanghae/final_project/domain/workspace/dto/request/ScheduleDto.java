package com.hanghae.final_project.domain.workspace.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ScheduleDto {
    private String content;
    private String eventDate;
}
