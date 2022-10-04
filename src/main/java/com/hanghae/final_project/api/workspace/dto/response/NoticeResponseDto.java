package com.hanghae.final_project.api.workspace.dto.response;


import com.hanghae.final_project.domain.model.Notice;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NoticeResponseDto {

    private Long id;
    private String title;
    private String content;
    private Long workspaceId;
    private String nickname;
    private String createdAt;
    private String modifiedAt;
    private String profileImage;
    private String username;


    public static NoticeResponseDto of(Notice notice) {
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .profileImage(notice.getUser().getProfileImage())
                .username(notice.getUser().getUsername())
                .workspaceId(notice.getWorkSpace().getId())
                .title(notice.getTitle())
                .nickname(notice.getUser().getNickname())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .modifiedAt(notice.getModifiedAt())
                .build();
    }
}





