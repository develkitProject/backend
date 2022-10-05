package com.hanghae.final_project.api.workspace.dto.response;

import com.hanghae.final_project.domain.model.Document;
import com.hanghae.final_project.domain.model.Timestamped;
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
    private String username;
    private String title;
    private String content;
    private List<String> fileNames;
    private List<String> fileUrls;
    private String nickname;
    private Long workSpaceId;
    private String createdAt;
    private String modifiedAt;
    private List<String> readMember;
    private String modifyMember;


    public static DocumentResponseDto of (Document document){
       return  DocumentResponseDto.builder()
                .id(document.getId())
                .createdAt(document.getCreatedAt())
                .title(document.getTitle())
                .nickname(document.getUser().getNickname())
                .modifiedAt(document.getModifiedAt())
                .workSpaceId(document.getWorkSpace().getId())
                .build();
    }

}
