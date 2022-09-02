package com.hanghae.final_project.domain.workspace.dto.response;

import com.hanghae.final_project.domain.workspace.model.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DocumentResponseDto extends Timestamped {

    private Long id;
    private String title;
    private String content;
//    private String imageUrl;

}
