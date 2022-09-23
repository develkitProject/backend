package com.hanghae.final_project.domain.workspace.dto.response;

import com.hanghae.final_project.domain.workspace.model.Document;
import com.hanghae.final_project.domain.workspace.model.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DocumentResponseDto extends Timestamped {

    private Long id;
    private String title;
    private String content;
    private List<String> fileUrls;
    private String nickname;
    private Long workSpaceId;
    private String createdAt;
    private String modifiedAt;
    private List<String> readMember;


    public static DocumentResponseDto of (Document document){
       return  DocumentResponseDto.builder()
                .id(document.getId())
                .createdAt(document.getModifiedAt())
                .title(document.getTitle())
                .nickname(document.getUser().getNickname())
                .modifiedAt(document.getModifiedAt())
                .workSpaceId(document.getWorkSpace().getId())
                .build();
    }

}
