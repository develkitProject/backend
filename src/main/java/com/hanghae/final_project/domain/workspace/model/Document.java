package com.hanghae.final_project.domain.workspace.model;


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
public class Document extends Timestamped{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    //DATATYPE = TEXT, MIDTEXT 이런것들이 있는지 확인하고 다른 표현 방식이 있으면 수정..
    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_user_id", nullable = false)
    private WorkSpaceUser workSpaceUser;

    private String imageUrl = null;
}

