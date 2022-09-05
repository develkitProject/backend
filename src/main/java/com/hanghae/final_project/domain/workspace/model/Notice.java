package com.hanghae.final_project.domain.workspace.model;

import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.workspace.dto.NoticeRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity

public class Notice extends Timestamped{


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkSpace workSpace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String imageUrl =null;

//    public static Notice of(NoticeRequestDto noticeRequestDto, WorkSpace workSpace) {
//        return Notice.builder()
//                .title(noticeRequestDto.getTitle())
//                .content(noticeRequestDto.getContent())
//                .workSpace(workSpace)
//                .build();
//    }

    public void updateNotice(NoticeRequestDto noticeRequestDto) {
        this.title = noticeRequestDto.getTitle();
        this.content = noticeRequestDto.getContent();
    }
}