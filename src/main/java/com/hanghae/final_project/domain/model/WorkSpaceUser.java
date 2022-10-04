package com.hanghae.final_project.domain.model;

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
//@Table(name="workspace_users")
public class WorkSpaceUser {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkSpace workSpace;


    public static WorkSpaceUser of(User user, WorkSpace workSpace) {
        return WorkSpaceUser.builder()
                .user(user)
                .workSpace(workSpace)
                .build();
    }

    public void update(WorkSpace workSpace) {
        this.workSpace = workSpace;
    }
}