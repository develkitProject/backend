package com.hanghae.final_project.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.final_project.api.workspace.dto.request.DocumentRequestDto;
import lombok.*;

import javax.persistence.*;


@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Document extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    //DATATYPE = TEXT, MIDTEXT 이런것들이 있는지 확인하고 다른 표현 방식이 있으면 수정..
    @Lob
    @Column
    private String content;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @JoinColumn(name="upadte_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User modifyUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkSpace workSpace;



    public Document(DocumentRequestDto documentRequestDto) {
        this.title = documentRequestDto.getTitle();
        this.content = documentRequestDto.getContent();

    }


    public void update(DocumentRequestDto documentRequestDto,User user) {
        this.title = documentRequestDto.getTitle();
        this.content = documentRequestDto.getContent();
        this.modifyUser=user;
    }
}
