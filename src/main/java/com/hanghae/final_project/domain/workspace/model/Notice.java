package com.hanghae.final_project.domain.workspace.model;

import com.hanghae.final_project.domain.workspace.notice.dto.NoticeRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity

public class Notice extends Timestamped{


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; //pk 유일한값.

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false) //fk
    private Notice workSpace;


    public Notice(NoticeRequestDto requestDto, User user,WorkSpace  workSpace) {
        this.title = requestDto.getTitle();
        this.user = user;
        this.workSpace = workSpace;
    }

    public void update(NoticeRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();

    }
}