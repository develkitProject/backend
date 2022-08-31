package com.hanghae.final_project.domain.workspace.dto.response;

import com.hanghae.final_project.domain.workspace.model.Timestamped;
import lombok.Getter;

@Getter
public class DocumentListResponseDto extends Timestamped {
    private String title;
}
