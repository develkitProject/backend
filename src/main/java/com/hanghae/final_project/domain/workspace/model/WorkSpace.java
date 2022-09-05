package com.hanghae.final_project.domain.workspace.model;


import com.hanghae.final_project.domain.workspace.dto.request.WorkspaceRequestDto;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class WorkSpace extends Timestamped implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    /*@JsonIgnore
    @OneToOne(mappedBy = "workSpace", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    private Invitation invitation;*/

    /*@JsonIgnore
    @OneToMany(mappedBy = "workSpace", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<WorkSpaceUser> workSpaceUsers = new ArrayList<>();*/

    public static WorkSpace of(WorkspaceRequestDto requestDto) {
        return WorkSpace.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
    }

    public WorkSpace(WorkspaceRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void update(WorkspaceRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
