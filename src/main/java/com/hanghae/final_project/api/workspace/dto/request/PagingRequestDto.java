package com.hanghae.final_project.api.workspace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingRequestDto {
    private String direction;
    private Long cursorId;
}
