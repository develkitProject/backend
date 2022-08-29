package com.hanghae.final_project.domain.workspace.document.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DocumentResponseDto {

    private Long id;
    private String title;
    private String content;
    private String imageUrl;

}
