package com.hanghae.final_project.domain.workspace.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.workspace.dto.request.WorkspaceRequestDto;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
@Builder
public class WorkSpace extends Timestamped implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String imageUrl;

    /*@JsonIgnore
    @OneToOne(mappedBy = "workSpace", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    private Invitation invitation;*/

    @JsonIgnore
    @OneToMany(mappedBy = "workSpace", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<WorkSpaceUser> workSpaceUsers = new ArrayList<>();

    // workspace를 만든 사람의 정보
    @JoinColumn(name = "createdByUser_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    @Column(nullable = false)
    private String invite_code;

    @Builder
    public static WorkSpace of(WorkspaceRequestDto requestDto, String imageUrl, User user) {
        return WorkSpace.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .createdBy(user)
                .imageUrl(imageUrl)
                .invite_code(UUID.randomUUID().toString())
                .build();
    }

    public void update(WorkspaceRequestDto requestDto, String imageUrl) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.imageUrl = imageUrl;
    }
}
