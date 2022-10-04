package com.hanghae.final_project.api.workspace.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ScheduleRequestDto {
    private String content;
    private String eventDate;
}
