package com.hanghae.final_project.domain.workspace.model;

import com.hanghae.final_project.domain.user.model.User;
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

public class Notice extends Timestamped {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkSpaceUser workSpaceUser;


    private String imageUrl = null;

}

// 전체조회 -> 워크스페이스 번호에따라서 있는 리스트들을 다 가져와야함
// 우리가 레포지토리에서 찾을 수 있는건 WorkSpaceUser로만 찾을 수 있기 때문에 전체조회를 못함