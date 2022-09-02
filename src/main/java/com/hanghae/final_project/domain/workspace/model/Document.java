package com.hanghae.final_project.domain.workspace.model;


import com.hanghae.final_project.domain.workspace.dto.request.DocumentRequestDto;
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
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkSpace workSpace;

//    private String imageUrl = null;

    public Document(DocumentRequestDto documentRequestDto) {
        this.title = documentRequestDto.getTitle();
        this.content = documentRequestDto.getContent();
//        this.imageUrl = documentRequestDto.getImageUrl();

    }


    public void update(DocumentRequestDto documentRequestDto) {
        this.title = documentRequestDto.getTitle();
        this.content = documentRequestDto.getContent();
//        this.imageUrl = documentRequestDto.getImageUrl();
    }
}

